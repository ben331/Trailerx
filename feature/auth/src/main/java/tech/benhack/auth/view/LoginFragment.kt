package tech.benhack.auth.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.facebook.CallbackManager
import tech.benhack.auth.R
import tech.benhack.auth.datasource.remote.ProviderType
import tech.benhack.auth.viewmodel.AuthViewModel
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.helpers.FormValidator
import tech.benhack.ui.TokenService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import tech.benhack.auth.databinding.FragmentLoginBinding
import tech.benhack.auth.datasource.remote.response.UserModel

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup
        setupNavBar()
        setupLiveData()
        setupButtons()
        setupForm()
        setupWatcher()
    }

    private fun setupNavBar(){
        binding.labelRegister.setOnClickListener{
            hideKeyboard()
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            navController.navigate(action)
        }
    }

    private fun setupLiveData(){
        authViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            enableButtons(!isLoading)
            if(isLoading){
                binding.progressComponent.visibility = View.VISIBLE
            }else{
                binding.progressComponent.visibility = View.GONE
            }
        }
    }

    private fun setupButtons(){
        binding.labelGuest.setOnClickListener {
            showHome("", ProviderType.GUEST)
        }

        binding.btnLogin.setOnClickListener{
            hideKeyboard()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            authViewModel.isLoading.postValue(true)
            authViewModel.loginWithEmailAndPassword(email, password, ::createUser, ::handleFailure)
        }

        binding.labelForgotPassword.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            if(FormValidator.validateEmail(email)){
                authViewModel.sendPasswordResetEmail(email, {
                    dialogManager.showAlert(requireContext(), getString(R.string.success),getString(R.string.password_reset_success))
                },::handleFailure)
            }else{
                dialogManager.showAlert(requireContext(), getString(R.string.error), getString(R.string.invalid_email))
            }
        }

        binding.googleBtn.setOnClickListener {
            val googleIntent = authViewModel.loginWithGoogle(requireActivity())
            authViewModel.isLoading.postValue(true)
            googleLauncher.launch(googleIntent)
        }

        binding.facebookBtn.setOnClickListener{
            authViewModel.isLoading.postValue(true)
            authViewModel.loginWithFacebook(requireActivity(), ::createUser,::handleFailure)
        }

        binding.appleBtn.setOnClickListener{
            authViewModel.isLoading.postValue(true)
            authViewModel.loginWithApple(requireActivity(), ::createUser, ::handleFailure)
        }
    }

    private fun onGoogleResult(result:ActivityResult){
        if(result.resultCode == Activity.RESULT_OK){
            authViewModel.onGoogleResult(result.data, ::createUser, ::handleFailure)
        }
    }

    private fun setupForm(){
        with(binding.editTextEmail) {
            setOnFocusChangeListener { _, hasFocus ->
                error = if (!hasFocus && !FormValidator.validateEmail( text.toString())) {
                    getString(R.string.invalid_email)
                }else{
                    null
                }
            }}

        with(binding.editTextPassword) {
            setOnFocusChangeListener { _, hasFocus ->
                error = if (!hasFocus && !FormValidator.validatePassword( text.toString())) {
                    getString(R.string.invalid_password)
                }else{
                    null
                }
            }}
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        val currentView = requireView()
        inputMethodManager.hideSoftInputFromWindow(currentView.windowToken, 0)
    }

    private fun setupWatcher(){
        val watcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()
                if(FormValidator.validateLogin(email, password)){
                    binding.btnLogin.isEnabled = true
                    binding.editTextEmail.error = null
                    binding.editTextPassword.error = null
                }else{
                    binding.btnLogin.isEnabled = false
                }
            }
        }
        binding.editTextEmail.addTextChangedListener(watcher)
        binding.editTextPassword.addTextChangedListener(watcher)
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
        val accessToken = email.let{ TokenService().generateToken(requireContext(), email, provider.name)}
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
    private fun enableButtons(enable:Boolean){
        with(binding){
            val email =  binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            editTextEmail.isEnabled = enable
            editTextPassword.isEnabled = enable
            labelForgotPassword.isEnabled = enable
            btnLogin.isEnabled = enable && FormValidator.validateLogin(email, password)
            appleBtn.isEnabled = enable
            facebookBtn.isEnabled = enable
            googleBtn.isEnabled = enable
            labelRegister.isEnabled = enable
            labelGuest.isEnabled = enable
        }
    }

    private fun handleFailure(title:Int, msg:Int){
        authViewModel.isLoading.postValue(false)
        dialogManager.showAlert(requireContext(),title, msg)
    }
}