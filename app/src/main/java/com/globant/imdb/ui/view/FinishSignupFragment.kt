package com.globant.imdb.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentFinishSignupBinding


class FinishSignupFragment : Fragment() {

    private val binding:FragmentFinishSignupBinding by lazy {
        FragmentFinishSignupBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}