package com.globant.imdb.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.facebook.login.LoginManager
import com.globant.imdb.R
import com.globant.imdb.data.remote.firebase.ProviderType
import com.globant.imdb.databinding.FragmentNavigationBinding
import com.globant.imdb.ui.viewmodel.AuthViewModel

class NavigationFragment : Fragment() {

    private val navController: NavController by lazy {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navHostFragment.navController
    }

    private val args: NavigationFragmentArgs by navArgs()

    private val binding:FragmentNavigationBinding by lazy {
        FragmentNavigationBinding.inflate(layoutInflater)
    }

    private val authViewModel:AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        saveSession()
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
        binding.navBar.setupWithNavController(navController)
        authViewModel.setupName(::setupNavBar)
    }

    private fun setupNavBar(remoteDisplayName: String?){
        val profileItem = binding.navBar.menu.findItem(R.id.profileFragment)
        profileItem.title = remoteDisplayName
    }

    private fun saveSession(){
        val email = args.email
        val provider = args.provider

        val prefs = activity?.
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()!!
        prefs.putString("email", email)
        prefs.putString("provider", provider.name)
        prefs.apply()
    }

    private fun cleanSession(){
        val prefs = activity?.
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()!!
        prefs.clear()
        prefs.apply()

        val provider = args.provider

        if(provider == ProviderType.FACEBOOK){
            LoginManager.getInstance().logOut()
        }
    }
}