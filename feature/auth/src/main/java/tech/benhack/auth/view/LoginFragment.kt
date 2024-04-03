package tech.benhack.auth.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import tech.benhack.auth.R
import tech.benhack.auth.datasource.remote.ProviderType
import tech.benhack.auth.viewmodel.AuthViewModel
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.helpers.FormValidator
import tech.benhack.ui.helpers.TokenUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import tech.benhack.auth.databinding.FragmentLoginBinding
import tech.benhack.auth.datasource.remote.response.UserModel
import tech.benhack.auth.view.screens.LoginScreen
import tech.benhack.ui.theme.TrailerxTheme

private const val HOME_URI = "android-app://tech.benhack.trailerx/home?token={tokenValue}"

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val authViewModel:AuthViewModel by activityViewModels()

    @Inject
    lateinit var callbackManager:CallbackManager
    @Inject
    lateinit var dialogManager: DialogManager

    private val binding:FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private val navController:NavController by lazy {
        findNavController()
    }

    private val googleLauncher =
        registerForActivityResult(StartActivityForResult(), ::onGoogleResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadSession()
    }

    override fun onResume() {
        super.onResume()
        binding.loginComposeView.disposeComposition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.loginComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val isLoading by authViewModel.isLoading.observeAsState(initial = false)

                TrailerxTheme {
                    LoginScreen(
                        isLoading = isLoading,
                        onForgotPassword = ::sendPasswordResetEmail,
                        onLoginWithEmailAndPassword = ::loginWithEmailAndPassword,
                        onLoginWithGoogle = ::loginWithGoogle,
                        onRegister = {
                            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                            navController.navigate(action)
                        },
                        onLoginAsGuest = {
                            showHome("", ProviderType.GUEST)
                        }
                    )
                }
            }
        }
        return binding.root
    }

    private fun sendPasswordResetEmail(email:String){
        if(FormValidator.validateEmail(email)){
            authViewModel.sendPasswordResetEmail(email, {
                dialogManager.showAlert(requireContext(), getString(R.string.success),getString(R.string.password_reset_success))
            },::handleFailure)
        }else{
            dialogManager.showAlert(requireContext(), getString(R.string.error), getString(R.string.invalid_email))
        }
    }

    private fun loginWithEmailAndPassword(email:String, password:String) {
        hideKeyboard()
        authViewModel.isLoading.postValue(true)
        authViewModel.loginWithEmailAndPassword(email, password, ::createUser, ::handleFailure)
    }

    private fun loginWithGoogle(){
        val googleIntent = authViewModel.loginWithGoogle(requireActivity())
        authViewModel.isLoading.postValue(true)
        googleLauncher.launch(googleIntent)
    }

    private fun loginWithFacebook(){
        authViewModel.isLoading.postValue(true)
        authViewModel.loginWithFacebook(requireActivity(), ::createUser,::handleFailure)
    }

    private fun loginWithApple(){
        authViewModel.isLoading.postValue(true)
        authViewModel.loginWithApple(requireActivity(), ::createUser, ::handleFailure)
    }

    private fun onGoogleResult(result:ActivityResult){
        if(result.resultCode == Activity.RESULT_OK){
            authViewModel.onGoogleResult(result.data, ::createUser, ::handleFailure)
        }else{
            dialogManager.showAlert(requireContext(), R.string.error, R.string.auth_error)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        val currentView = requireView()
        inputMethodManager.hideSoftInputFromWindow(currentView.windowToken, 0)
    }

    private fun loadSession(){
        val prefs = activity?.
        getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val token = prefs?.getString("token", null)
        token?.let { showHome(token) }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.isLoading.postValue(false)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun showHome(email:String, provider: ProviderType) {
        val accessToken = email.let{ TokenUtil().generateToken(requireContext(), email, provider.name)}
        showHome(accessToken)
    }

    private fun showHome(token:String){

        authViewModel.isLoading.postValue(false)

        val request = NavDeepLinkRequest.Builder
            .fromUri(HOME_URI.replace("{tokenValue}", token).toUri())
            .build()

        // By default, implicit deeplink does not clean backstack, options is necessary.
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.login_nav_graph, true)
            .build()

        navController.navigate(request, options)
    }
    private fun createUser(email:String, providerType: ProviderType){
        val user = UserModel(email, authViewModel.getDisplayName())
        authViewModel.createUser(user) { userCreated ->
            if (userCreated) {
                showHome(email, providerType)
            } else {
                authViewModel.logout(providerType)
                dialogManager.showAlert(
                    requireContext(),
                    getString(R.string.error),
                    getString(R.string.create_user_error)
                )
            }
        }
    }

    private fun handleFailure(title:Int, msg:Int){
        authViewModel.isLoading.postValue(false)
        dialogManager.showAlert(requireContext(),title, msg)
    }
}