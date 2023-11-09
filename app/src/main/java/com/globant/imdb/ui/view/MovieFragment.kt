package com.globant.imdb.ui.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.globant.imdb.R
import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.core.TextTransforms
import com.globant.imdb.databinding.FragmentMovieBinding
import com.globant.imdb.ui.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()

    private val args: MovieFragmentArgs by navArgs()

    private val binding: FragmentMovieBinding by lazy {
        FragmentMovieBinding.inflate(layoutInflater)
    }

    private val navController by lazy {
        findNavController()
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
        setupTopAppBar()
        setupLiveData()
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        movieViewModel.onCreate(args.movieId)
    }

    private fun setupTopAppBar(){
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupLiveData(){
        movieViewModel.currentMovie.observe(viewLifecycleOwner){ movieDetail ->
            movieViewModel.recordHistory(requireContext())
            binding.topAppBar.title = movieDetail.title
            with(binding.containerFrontage){
                sectionTitle.text = movieDetail.title
                originTitle.text = movieDetail.originalTitle
                detailMovie.text =
                    TextTransforms.createDescription(movieDetail.tagline, movieDetail.releaseDate)
            }
            with(binding.containerSypnosis){
                labelGenre.text =
                    if(movieDetail.genres.isNotEmpty()){
                        movieDetail.genres[0].name
                    }else{ "- - - -" }
                labelStars.text = movieDetail.popularity.toString()
                textBoxSynopsis.text = movieDetail.overview
                val url = RetrofitHelper.IMAGES_BASE_URL + movieDetail.backdropPath
                Picasso.with(requireContext())
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(imgMovie)
            }
            movieViewModel.isLoading.postValue(false)
        }

        movieViewModel.videoIframe.observe(viewLifecycleOwner){
            movieViewModel.videoIframe.observe(viewLifecycleOwner) { videoIframe ->
                videoIframe?.let {
                    with(binding.containerFrontage.videoMovie){
                        loadData(it, "text/html", "utf-8")
                        settings.javaScriptEnabled = true
                        webChromeClient = WebChromeClient()
                    }
                }
                movieViewModel.isLoading.postValue(false)
            }
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = it
        }
    }

    private fun setupButtons(){
        binding.btnActionList.setOnClickListener{
            movieViewModel.addMovieToWatchList(requireContext()) {
                showAlert(
                    getString(R.string.success),
                    "Movie ${it.title} added successfully"
                )
            }
        }
        binding.refreshLayout.setOnRefreshListener {
            movieViewModel.onCreate(args.movieId)
        }
    }

    private fun showAlert(title:String, message:String){
        movieViewModel.isLoading.postValue(false)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}