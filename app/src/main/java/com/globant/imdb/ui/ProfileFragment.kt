package com.globant.imdb.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val listener: LogOutListener by lazy {
        // TODO: resolve error: find fragment return null
        parentFragmentManager.findFragmentById(R.id.nav_host_fragment) as LogOutListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup(){
        // TODO: Update click event
        binding.profileHeaderContainer.btnSettings.setOnClickListener{
            listener.logOut()
        }
    }

    interface LogOutListener {
        fun logOut()
    }
}