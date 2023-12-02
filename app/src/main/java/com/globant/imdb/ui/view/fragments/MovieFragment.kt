package com.globant.imdb.ui.view.fragments

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
import com.globant.imdb.core.Constants
import com.globant.imdb.ui.helpers.DialogManager
import com.globant.imdb.ui.helpers.TextTransforms
import com.globant.imdb.databinding.FragmentMovieBinding
import com.globant.imdb.ui.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

    @Inject
    lateinit var dialogManager: DialogManager

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
        movieViewModel.onRefresh(args.movieId)
    }

    private fun setupTopAppBar(){
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun setupLiveData(){
        movieViewModel.currentMovie.observe(viewLifecycleOwner){ movieItem ->
            movieViewModel.recordHistory(::handleFailure)
            binding.topAppBar.title = movieItem.title
            with(binding.containerFrontage){
                sectionTitle.text = movieItem.title
                originTitle.text = movieItem.originalTitle
                detailMovie.text =
                    TextTransforms.createDescription(movieItem.tagline, movieItem.releaseDate)
            }
            with(binding.containerSypnosis){
                labelGenre.text =
                    if(!movieItem.genreNames.isNullOrEmpty()){
                        movieItem.genreNames[0]
                    }else{ "- - - -" }
                labelStars.text = movieItem.popularity.toString()
                textBoxSynopsis.text = movieItem.overview
                val url = Constants.IMAGES_BASE_URL + movieItem.backdropPath
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
            movieViewModel.addMovieToWatchList({
                movieViewModel.isLoading.postValue(false)
                dialogManager.showAlert(
                    requireContext(),
                    getString(R.string.success),
                    getString(R.string.success_movie_added, it.title)
                )
            }, ::handleFailure)
        }
        binding.refreshLayout.setOnRefreshListener {
            movieViewModel.onRefresh(args.movieId)
        }
    }

    private fun handleFailure(title:Int, msg:Int){
        movieViewModel.isLoading.postValue(false)
        dialogManager.showAlert(requireContext(),title, msg)
    }
}