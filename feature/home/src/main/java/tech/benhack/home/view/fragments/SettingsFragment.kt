package tech.benhack.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tech.benhack.home.R
import tech.benhack.home.databinding.FragmentSettingsBinding
import tech.benhack.home.view.screens.SettingsScreen
import tech.benhack.home.viewmodel.SettingsViewModel
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.theme.TrailerxTheme
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
        binding.settingsComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val isLoading by viewModel.isLoading.observeAsState(initial = false)

                TrailerxTheme {
                    SettingsScreen(
                        isLoading = isLoading,
                        isGuest = viewModel.email.isBlank(),
                        onNavigateBack = { navController.popBackStack() },
                        onLogout = ::logout,
                        onDeleteAccount = {
                            dialogManager.showDialog(
                                requireContext(),
                                R.string.warning,
                                R.string.alert_delete_account
                            ) { viewModel.deleteAccount(requireContext()) }
                        }
                    )
                }
            }
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeListener()
        setupLiveData()
    }

    private fun subscribeListener(){
        val parent = parentFragment?.parentFragment as NavigationFragment
        logoutListener = parent
    }

    private fun setupLiveData(){
        viewModel.accountDeleted.observe(viewLifecycleOwner){ accountDeleted ->
            if(accountDeleted){
                logout()
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