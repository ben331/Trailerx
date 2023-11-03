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
import com.globant.imdb.ui.viewmodel.MovieDetailViewModel
import com.squareup.picasso.Picasso

class MovieFragment : Fragment() {

    private val movieDetailViewModel: MovieDetailViewModel by viewModels()

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
        loadData()
    }

    private fun setupTopAppBar(){
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupLiveData(){
        movieDetailViewModel.currentMovie.observe(viewLifecycleOwner){ movieDetail ->
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
        }

        movieDetailViewModel.videoIframe.observe(viewLifecycleOwner){
            movieDetailViewModel.videoIframe.observe(viewLifecycleOwner) { videoIframe ->
                videoIframe?.let {
                    with(binding.containerFrontage.videoMovie){
                        loadData(it, "text/html", "utf-8")
                        settings.javaScriptEnabled = true
                        webChromeClient = WebChromeClient()
                    }
                }
                movieDetailViewModel.isLoading.postValue(false)
            }
        }

        movieDetailViewModel.isLoading.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = it
        }
    }

    private fun setupButtons(){
        binding.btnActionList.setOnClickListener{
            movieDetailViewModel.addMovieToWatchList(requireContext()) {
                showAlert(
                    getString(R.string.success),
                    "Movie ${it.title} added successfully"
                )
            }
        }
        binding.refreshLayout.setOnRefreshListener {
            movieDetailViewModel.onCreate(args.movieId)
        }
    }

    private fun loadData(){
        args.movieId.let {  movieId ->
            movieDetailViewModel.onCreate(movieId)
        }
    }

    private fun showAlert(title:String, message:String){
        movieDetailViewModel.isLoading.postValue(false)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}