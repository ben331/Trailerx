package com.globant.imdb.ui.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.imdb.R
import com.globant.imdb.core.Constants
import com.globant.imdb.core.DialogManager
import com.globant.imdb.databinding.FragmentHomeBinding
import com.globant.imdb.ui.view.adapters.MovieAdapter
import com.globant.imdb.ui.view.adapters.MovieViewHolder
import com.globant.imdb.ui.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), MovieAdapter.ImageRenderListener, MovieViewHolder.MovieListener {

    @Inject
    lateinit var dialogManager: DialogManager

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var nowPlayingMoviesAdapter: MovieAdapter
    private lateinit var upcomingMoviesAdapter: MovieAdapter
    private lateinit var popularMoviesAdapter: MovieAdapter

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

    private fun setupButtons(){
        binding.refreshLayout.setOnRefreshListener(::refresh)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupLiveData(){
        homeViewModel.mainMovie.observe(viewLifecycleOwner) { currentMovie ->
            with(binding.mainTrailerContainer) {
                trailerName.text = currentMovie.title
                val imageUrl = Constants.IMAGES_BASE_URL + currentMovie.backdropPath
                Picasso.with(requireContext())
                    .load(imageUrl)
                    .fit()
                    .centerCrop()
                    .into(trailerImageView)

                homeViewModel.getTrailerOfMovie(currentMovie.id)
            }
        }

        homeViewModel.videoIframe.observe(viewLifecycleOwner) { videoIframe ->
            videoIframe?.let {
                with(binding.mainTrailerContainer.trailerWebView){
                    loadData(it, "text/html", "utf-8")
                    settings.javaScriptEnabled = true
                    webChromeClient = WebChromeClient()
                }
                homeViewModel.isLoading.postValue(false)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = it
        }

        homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner){
            nowPlayingMoviesAdapter.movieList = it
            nowPlayingMoviesAdapter.notifyDataSetChanged()
        }
        homeViewModel.upcomingMovies.observe(viewLifecycleOwner){
            upcomingMoviesAdapter.movieList = it
            upcomingMoviesAdapter.notifyDataSetChanged()
        }
        homeViewModel.popularMovies.observe(viewLifecycleOwner){
            popularMoviesAdapter.movieList = it
            popularMoviesAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerViews(){
        nowPlayingMoviesAdapter = MovieAdapter()
        upcomingMoviesAdapter = MovieAdapter()
        popularMoviesAdapter = MovieAdapter()

        nowPlayingMoviesAdapter.numberList = 1
        upcomingMoviesAdapter.numberList = 2
        popularMoviesAdapter.numberList = 3

        nowPlayingMoviesAdapter.moviesListener = this
        upcomingMoviesAdapter.moviesListener = this
        popularMoviesAdapter.moviesListener = this

        with(binding.listMoviesOne){
            titleContainer.sectionTitle.text = getString(R.string.section_now_playing)
            listDescription.visibility = View.GONE
            moviesRecyclerView.adapter = nowPlayingMoviesAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

        with(binding.listMoviesTwo){
            titleContainer.sectionTitle.text = getString(R.string.section_upcoming)
            listDescription.visibility = View.GONE
            moviesRecyclerView.adapter = upcomingMoviesAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

        with(binding.listMoviesTree){
            titleContainer.sectionTitle.text = getString(R.string.section_popular)
            listDescription.visibility = View.GONE
            moviesRecyclerView.adapter = popularMoviesAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }
    }

    private fun refresh(){
        homeViewModel.onCreate()
    }

    override fun renderImage(url: String, image: ImageView) {
        Picasso.with(requireContext())
            .load(url)
            .fit()
            .centerCrop()
            .into(image)
    }

    override fun showDetails(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(id)
        findNavController().navigate(action)
    }

    override fun addToList(id: Int, numberList:Int) {
        homeViewModel.addMovieToWatchList(id, numberList){
            homeViewModel.isLoading.postValue(false)
            dialogManager.showAlert(
                getString(R.string.success),
                getString(R.string.success_movie_added, it.title)
            )
        }
    }
}