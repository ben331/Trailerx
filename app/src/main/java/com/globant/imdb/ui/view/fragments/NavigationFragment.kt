package com.globant.imdb.ui.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentNavigationBinding
import com.globant.imdb.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationFragment : Fragment(), PopupMenu.OnMenuItemClickListener, ProfileFragment.LogoutListener {

    private val navController: NavController by lazy {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navHostFragment.navController
    }

    private val args: NavigationFragmentArgs by navArgs()

    private val binding:FragmentNavigationBinding by lazy {
        FragmentNavigationBinding.inflate(layoutInflater)
    }

    private val authViewModel:AuthViewModel by activityViewModels()

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
        authViewModel.setupName(::setupNavBar)
    }

    @Suppress("DEPRECATION")
    private fun setupNavBar(remoteDisplayName: String?){
        binding.navBar.setupWithNavController(navController)
        val profileItem = binding.navBar.menu.findItem(R.id.profileFragment)
        profileItem.title = remoteDisplayName ?: getString(R.string.guest_name)
        binding.navBar.setOnNavigationItemSelectedListener {
            with(childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment){
                if(childFragmentManager.fragments[0] is MovieFragment){
                    findNavController().popBackStack()
                }
            }
            true
        }
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.item_logout -> logout()
        }
        return true
    }

    override fun logout(){
        cleanSession()
        val action = NavigationFragmentDirections.actionNavigationFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun cleanSession(){
        val prefs = activity?.
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()!!
        prefs.clear()
        prefs.apply()
        authViewModel.logout(args.provider)
    }
}