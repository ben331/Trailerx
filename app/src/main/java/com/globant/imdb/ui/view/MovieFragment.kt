package com.globant.imdb.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentHomeBinding
import com.globant.imdb.databinding.FragmentMovieBinding

class MovieFragment : Fragment() {
    private val binding: FragmentMovieBinding by lazy {
        FragmentMovieBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}