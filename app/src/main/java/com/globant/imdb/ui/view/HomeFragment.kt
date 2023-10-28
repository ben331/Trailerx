package com.globant.imdb.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.imdb.R
import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.databinding.FragmentHomeBinding
import com.globant.imdb.ui.view.adapters.MovieAdapter
import com.globant.imdb.ui.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso


class HomeFragment : Fragment(), MovieAdapter.ImageRenderListener {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var nowPlayingMoviesAdapter: MovieAdapter
    private lateinit var upcomingMoviesAdapter: MovieAdapter
    private lateinit var popularMoviesAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitHelper.authToken = getString(R.string.TMDB_api_token)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup
        setupButtons()
        setupLiveData()
        setupRecyclerViews()

        refresh()
    }

    private fun setupRecyclerViews(){
        nowPlayingMoviesAdapter = MovieAdapter()
        upcomingMoviesAdapter = MovieAdapter()
        popularMoviesAdapter = MovieAdapter()

        nowPlayingMoviesAdapter.movieList = movieViewModel.nowPlayingMovies
        upcomingMoviesAdapter.movieList = movieViewModel.upcomingMovies
        popularMoviesAdapter.movieList = movieViewModel.popularMovies

        nowPlayingMoviesAdapter.imageRenderListener = this
        upcomingMoviesAdapter.imageRenderListener = this
        popularMoviesAdapter.imageRenderListener = this

        with(binding.listMoviesOne){
            titleContainer.sectionTitle.text = getString(R.string.section_now_playing)
            listDescription.visibility = View.GONE
            btnActionList.visibility = View.GONE
            moviesRecyclerView.adapter = nowPlayingMoviesAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

        with(binding.listMoviesTwo){
            titleContainer.sectionTitle.text = getString(R.string.section_upcoming)
            listDescription.visibility = View.GONE
            btnActionList.visibility = View.GONE
            moviesRecyclerView.adapter = upcomingMoviesAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

        with(binding.listMoviesTree){
            titleContainer.sectionTitle.text = getString(R.string.section_popular)
            listDescription.visibility = View.GONE
            btnActionList.visibility = View.GONE
            moviesRecyclerView.adapter = popularMoviesAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

    }

    private fun setupButtons(){
        binding.refreshLayout.setOnRefreshListener(::refresh)
    }

    private fun setupLiveData(){
        movieViewModel.mainMovie.observe(viewLifecycleOwner) { currentMovie ->
            binding.refreshLayout.isRefreshing = false
            with(binding.mainTrailerContainer) {
                trailerName.text = currentMovie.title
                val imageUrl = RetrofitHelper.imageUrl + currentMovie.backdropPath
                Picasso.with(requireContext())
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .into(trailerImageView)
            }
        }
    }

    private fun refresh(){
        movieViewModel.onCreate(
            nowPlayingMoviesAdapter,
            upcomingMoviesAdapter,
            popularMoviesAdapter
        )
    }

    private fun showAlert(message:String){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.error)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun renderImage(url: String, image: ImageView) {
        Picasso.with(requireContext())
            .load(url)
            .fit()
            .centerCrop()
            .into(image)
    }
}