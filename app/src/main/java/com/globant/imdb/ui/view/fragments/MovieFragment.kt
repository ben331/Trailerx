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
import com.globant.imdb.ui.helpers.ImageLoader
import com.globant.imdb.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var imageLoader: ImageLoader

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

        if(movieViewModel.username.isNotEmpty()){ movieViewModel.recordHistory() }
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
        movieViewModel.isVideoAvailable.observe(viewLifecycleOwner, ::turnOfflineMode)

        movieViewModel.currentMovie.observe(viewLifecycleOwner){ movieDetailItem ->

            if(movieDetailItem == null) {
                dialogManager.showAlert (
                    requireContext(),
                    getString(R.string.error),
                    getString(R.string.get_movie_error)
                )
                findNavController().popBackStack()
            } else {
                binding.topAppBar.title = movieDetailItem.title
                with(binding.containerFrontage){
                    sectionTitle.text = movieDetailItem.title
                    originTitle.text = movieDetailItem.originalTitle
                    detailMovie.text =
                        TextTransforms.createDescription(movieDetailItem.tagline, movieDetailItem.releaseDate)
                }
                with(binding.containerSypnosis){
                    if(movieDetailItem.tagline.isNullOrEmpty()){
                        labelGenre.visibility = View.GONE
                    }else{
                        labelGenre.visibility = View.VISIBLE
                        labelGenre.text = movieDetailItem.genres?.get(0)?.name
                    }
                    labelStars.text = movieDetailItem.popularity.toString()
                    textBoxSynopsis.text = movieDetailItem.overview
                }
                val url = Constants.IMAGES_BASE_URL + movieDetailItem.backdropPath
                imageLoader
                    .renderImageCenterCrop(requireContext(),url,binding.containerSypnosis.imgMovie)
                imageLoader
                    .renderImageCenterCrop(requireContext(),url,binding.containerFrontage.imgVideoMovie)
                movieViewModel.isLoading.postValue(false)
            }
        }

        movieViewModel.videoIframe.observe(viewLifecycleOwner){
            movieViewModel.videoIframe.observe(viewLifecycleOwner) { videoIframe ->
                videoIframe?.let {
                    with(binding.containerFrontage.videoMovie){
                        if(movieViewModel.isVideoAvailable.value == true){
                            loadData(it, "text/html", "utf-8")
                            settings.javaScriptEnabled = true
                            webChromeClient = WebChromeClient()
                        }
                    }
                }
                movieViewModel.isLoading.postValue(false)
            }
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = it
        }
    }

    private fun turnOfflineMode(isServiceAvailable: Boolean) {
        with(binding.containerFrontage){
            if(isServiceAvailable){
                videoMovie.visibility = View.VISIBLE
            } else {
                videoMovie.visibility = View.GONE
            }
        }
    }

    private fun setupButtons(){
        binding.btnActionList.setOnClickListener{
            if(movieViewModel.username.isNotEmpty()){
                movieViewModel.addMovieToWatchList(requireContext())
            }else{
                val action = MovieFragmentDirections.actionMovieFragmentToProfileFragment()
                findNavController().navigate(action)
            }

        }
        binding.refreshLayout.setOnRefreshListener {
            movieViewModel.onRefresh(args.movieId)
        }
    }


}