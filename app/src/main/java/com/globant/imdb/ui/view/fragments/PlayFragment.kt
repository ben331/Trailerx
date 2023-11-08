package com.globant.imdb.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentMovieBinding
import com.globant.imdb.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {
    private val binding: FragmentPlayBinding by lazy {
        FragmentPlayBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}