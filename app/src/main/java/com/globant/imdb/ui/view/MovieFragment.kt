package com.globant.imdb.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
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
                labelGenre.text = movieDetail.genres[0].name
                labelStars.text = movieDetail.popularity.toString()
                textBoxSynopsis.text = movieDetail.overview
                val url = RetrofitHelper.imageUrl + movieDetail.backdropPath
                Picasso.with(requireContext())
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(imgMovie)
            }
        }
    }

    private fun loadData(){
        args.movieId.let {  movieId ->
            movieDetailViewModel.onCreate(movieId)
        }
    }
}