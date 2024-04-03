package tech.benhack.auth.view

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import tech.benhack.auth.R
import tech.benhack.auth.databinding.FragmentSignUpBinding
import tech.benhack.auth.viewmodel.AuthViewModel
import tech.benhack.ui.helpers.DialogManager
import dagger.hilt.android.AndroidEntryPoint
import tech.benhack.auth.view.screens.SignupScreen
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject
    lateinit var dialogManager: DialogManager
    private val authViewModel:AuthViewModel by activityViewModels()

    private val binding:FragmentSignUpBinding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onResume() {
        super.onResume()
        binding.signupComposeView.disposeComposition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.signupComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val isLoading by authViewModel.isLoading.observeAsState(initial = false)

                MaterialTheme {
                    SignupScreen(
                        isLoading = isLoading,
                        onRegister = ::signUpWithEmailAndPassword,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
        return binding.root
    }

    private fun signUpWithEmailAndPassword(displayName:String, email:String, password:String){
        authViewModel.isLoading.postValue(true)
        hideKeyboard()
        authViewModel.signUpWithEmailAndPassword(
            email,
            password,
            displayName,
            ::showLogin,
            ::handleFailure,
        )
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentView = requireView()
        inputMethodManager.hideSoftInputFromWindow(currentView.windowToken, 0)
    }

    private fun showLogin(email: String?){
        authViewModel.isLoading.postValue(false)
        email?.let {
            dialogManager.showAlert(
                requireContext(),
                getString(R.string.success),
                getString(R.string.account_created_success)
            )
        }
        navController.popBackStack()
    }

    private fun handleFailure(title:Int, msg:Int){
        authViewModel.isLoading.postValue(false)
        dialogManager.showAlert(requireContext(),title, msg)
    }
}