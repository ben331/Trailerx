package com.globant.imdb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.globant.imdb.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val binding:FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.btnLogin.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}