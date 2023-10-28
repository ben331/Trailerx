package com.globant.imdb.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.globant.imdb.R
import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.databinding.FragmentHomeBinding
import com.globant.imdb.ui.viewmodel.MovieViewModel
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val movieViewModel: MovieViewModel by viewModels()

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
        movieViewModel.mainMovie.observe(viewLifecycleOwner) { currentMovie ->
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
        movieViewModel.onCreate()
    }

    private fun showAlert(message:String){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.error)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}