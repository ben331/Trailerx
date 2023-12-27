package com.globant.imdb.ui.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.globant.imdb.R
import com.globant.ui.TokenService
import com.globant.imdb.databinding.FragmentNavigationBinding
import com.globant.imdb.ui.viewmodel.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationFragment : Fragment(), PopupMenu.OnMenuItemClickListener, ProfileFragment.LogoutListener {

    private val navController: NavController by lazy {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navHostFragment.navController
    }

    private val viewModel:NavigationViewModel by viewModels()

    private val args: NavigationFragmentArgs by navArgs()

    private val binding:FragmentNavigationBinding by lazy {
        FragmentNavigationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveSession()
        preloadImages()
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.movieFragment){
               binding.navBar.visibility = View.GONE
            }else{
                binding.navBar.visibility = View.VISIBLE
            }
        }
        binding.navBar.setupWithNavController(navController)
        viewModel.setupName(::setupNavBar)

        viewModel.isImagesLoading.observe(viewLifecycleOwner){ isLoading ->
            showContent(!isLoading)
            showLoading(isLoading)
        }
    }

    private fun setupNavBar(remoteDisplayName: String?){
        val profileItem = binding.navBar.menu.findItem(R.id.profileFragment)
        profileItem.title = remoteDisplayName ?: getString(R.string.guest_name)
    }

    private fun saveSession(){
        args.email?.let { email ->
            val provider = args.provider
            val token = TokenService().generateToken(requireContext(), email, provider.name)
            val prefs = activity?.
            getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()!!
            prefs.putString("token", token)
            prefs.apply()
        }
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
        viewModel.logout(args.provider)
    }

    private fun preloadImages(){
        val prefs = activity?.
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val isFirstTime = prefs?.getBoolean("first_time", true) ?: true

        if(isFirstTime){
            viewModel.preloadUserDataAndImages(requireContext()) {
                prefs?.edit()!!.putBoolean("first_time", false).apply()
            }
        }
    }

    private fun showContent(show:Boolean){
        val visibility = if(show) View.VISIBLE else View.GONE
        with(binding){
            homeNavHost.visibility = visibility
            navBar.visibility = visibility
        }
    }

    private fun showLoading(show:Boolean){
        val visibility = if(show) View.VISIBLE else View.GONE
        binding.progressComponent.visibility = visibility
    }
}