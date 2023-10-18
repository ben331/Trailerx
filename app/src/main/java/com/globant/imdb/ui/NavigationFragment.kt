package com.globant.imdb.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentNavigationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class NavigationFragment : Fragment() {

    private lateinit var navController: NavController

    private val args: NavigationFragmentArgs by navArgs()

    private val binding:FragmentNavigationBinding by lazy {
        FragmentNavigationBinding.inflate(layoutInflater)
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navController = navHostFragment.navController

        setup()
    }

    private fun setup(){
        binding.navBar.setupWithNavController(navController)
        setupName()
    }

    private fun setupName() {
        args.displayName.let { displayName ->
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            auth.currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if(!task.isSuccessful){
                        showAlert()
                    }
                }
        }

        auth.currentUser?.displayName.let { remoteDisplayName ->
            val profileItem = binding.navBar.menu.findItem(R.id.profileFragment)
            profileItem.title = remoteDisplayName
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error actualizando el nombre del usuario")
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}