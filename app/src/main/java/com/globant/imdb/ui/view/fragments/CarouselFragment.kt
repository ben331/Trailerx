package com.globant.imdb.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globant.imdb.databinding.FragmentCarouselBinding

class CarouselFragment : Fragment() {

    private val binding: FragmentCarouselBinding by lazy {
        FragmentCarouselBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}