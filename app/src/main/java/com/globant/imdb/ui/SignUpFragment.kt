package com.globant.imdb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private val binding:FragmentSignUpBinding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.backButton.setOnClickListener{
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }
}