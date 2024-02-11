package tech.benhack.home.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import tech.benhack.home.R
import tech.benhack.home.databinding.FragmentSettingsBinding
import tech.benhack.home.viewmodel.SettingsViewModel
import tech.benhack.ui.helpers.DialogManager
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel:SettingsViewModel by viewModels()

    @Inject
    lateinit var dialogManager: DialogManager

    private val binding: FragmentSettingsBinding by lazy {
        FragmentSettingsBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    private lateinit var logoutListener: LogoutListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopAppBar()
        setupButtons()
        setupLiveData()
    }

    private fun setupTopAppBar(){
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.topAppBar.setupWithNavController(navController, appBarConfiguration)
        binding.topAppBar.title = getString(R.string.settings)
    }

    private fun setupButtons(){
        val parent = parentFragment?.parentFragment as NavigationFragment
        logoutListener = parent
        binding.btnLogout.setOnClickListener {
            logout()
        }

        if(viewModel.email.isBlank()){
            binding.btnDeleteAccount.visibility = View.GONE
        }else {
            binding.btnDeleteAccount.visibility = View.VISIBLE
        }

        binding.btnDeleteAccount.setOnClickListener {
            dialogManager.showDialog(
                requireContext(),
                R.string.warning,
                R.string.alert_delete_account
            ) { viewModel.deleteAccount(requireContext()) }
        }
    }

    private fun setupLiveData(){
        viewModel.accountDeleted.observe(viewLifecycleOwner){ accountDeleted ->
            if(accountDeleted){
                logout()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if(isLoading){
                binding.progressComponent.visibility = View.VISIBLE
                binding.topAppBar.visibility = View.GONE
            }else{
                binding.progressComponent.visibility = View.GONE
                binding.topAppBar.visibility = View.VISIBLE
            }
        }
    }

    private fun logout(){
        navController.popBackStack(R.id.home_nav_graph, true)
        logoutListener.logout()
    }

    interface LogoutListener {
        fun logout()
    }
}