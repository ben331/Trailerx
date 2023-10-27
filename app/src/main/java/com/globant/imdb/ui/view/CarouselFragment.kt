package com.globant.imdb.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentCarouselBinding
import com.globant.imdb.databinding.FragmentFinishSignupBinding

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