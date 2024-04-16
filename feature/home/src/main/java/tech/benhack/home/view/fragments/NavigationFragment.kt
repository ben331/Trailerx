package tech.benhack.home.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import tech.benhack.auth.datasource.remote.ProviderType
import tech.benhack.home.R
import tech.benhack.home.databinding.FragmentNavigationBinding
import tech.benhack.home.viewmodel.NavigationViewModel
import tech.benhack.ui.helpers.TokenUtil
import tech.benhack.ui.helpers.DialogManager
import dagger.hilt.android.AndroidEntryPoint

private const val AUTH_URI = "android-app://tech.benhack.trailerx/auth"

@AndroidEntryPoint
class NavigationFragment : Fragment(),
    SettingsFragment.LogoutListener {

    private val navController: NavController by lazy {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        navHostFragment.navController
    }

    private val viewModel:NavigationViewModel by viewModels()

    private val binding:FragmentNavigationBinding by lazy {
        FragmentNavigationBinding.inflate(layoutInflater)
    }

    private val args: NavigationFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkSession()
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
            if(destination.id == R.id.movieFragment || destination.id == R.id.settingsFragment){
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

    private fun checkSession() {
        val token =
            activity?.getSharedPreferences(
                getString(R.string.prefs_file),
                Context.MODE_PRIVATE
            )?.getString("token", null) ?: args.token

        val claims = TokenUtil().validateToken(requireContext(),token)

        //Decrypt Success
        if(claims!=null){
            saveSession(token)
        }//Decrypt Fail
        else{
            DialogManager().showAlert(requireContext(), "Error", "Invalid Token")
            logout()
        }
    }

    private fun saveSession(token:String) {
        val prefs = activity?.
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()!!
        prefs.putString("token", token)
        prefs.apply()
    }

    override fun logout(){
        cleanSession()
        val request = NavDeepLinkRequest.Builder
            .fromUri(AUTH_URI.toUri())
            .build()

        val options = NavOptions.Builder()
            .setPopUpTo(R.id.main_nav_graph, true)
            .build()

        findNavController().navigate(request, options)
    }

    private fun cleanSession(){
        val prefs = activity?.
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)?.edit()!!
        prefs.clear()
        prefs.apply()

        TokenUtil().validateToken(requireContext(), args.token)?.also {
            val provider = ProviderType.valueOf( it["provider"] as String )
            viewModel.logout(provider)
        }
    }

    private fun preloadImages(){
        val prefs = activity?.
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val isFirstTime = prefs?.getBoolean("first_time", true) ?: true

        if(isFirstTime){
            viewModel.preloadUserData(requireContext()) {
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