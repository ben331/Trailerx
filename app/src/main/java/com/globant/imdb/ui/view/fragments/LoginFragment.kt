package com.globant.imdb.ui.view.fragments

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.globant.imdb.R
import com.globant.imdb.core.DialogManager
import com.globant.imdb.core.FormValidator
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.data.network.firebase.ProviderType
import com.globant.imdb.databinding.FragmentLoginBinding
import com.globant.imdb.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val authViewModel:AuthViewModel by activityViewModels()

    @Inject
    lateinit var callbackManager:CallbackManager
    @Inject
    lateinit var dialogManager:DialogManager

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
            showHome(null, ProviderType.GUEST)
        }

        binding.btnLogin.setOnClickListener{
            hideKeyboard()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            authViewModel.isLoading.postValue(true)
            authViewModel.loginWithEmailAndPassword(email, password, ::createUser)
        }

        binding.labelForgotPassword.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            if(FormValidator.validateEmail(email)){
                authViewModel.sendPasswordResetEmail(email) {
                    dialogManager.showAlert(getString(R.string.success),getString(R.string.password_reset_success))
                }
            }else{
                dialogManager.showAlert(getString(R.string.error), getString(R.string.invalid_email))
            }
        }

        binding.googleBtn.setOnClickListener {
            val googleIntent = authViewModel.loginWithGoogle(requireActivity())
            authViewModel.isLoading.postValue(true)
            googleLauncher.launch(googleIntent)
        }

        binding.facebookBtn.setOnClickListener{
            authViewModel.isLoading.postValue(true)
            authViewModel.loginWithFacebook(requireActivity(), ::createUser)
        }

        binding.appleBtn.setOnClickListener{
            authViewModel.isLoading.postValue(true)
            authViewModel.loginWithApple(requireActivity(), ::createUser)
        }
    }

    private fun onGoogleResult(result:ActivityResult){
        if(result.resultCode == Activity.RESULT_OK){
            authViewModel.onGoogleResult(result.data, ::createUser)
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
        val email = prefs?.getString("email", null)
        val provider = prefs?.getString("provider", null)

        if(email!=null && provider!=null){
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authViewModel.isLoading.postValue(false)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun showHome(email:String?, providerType: ProviderType){
        authViewModel.isLoading.postValue(false)
        val action = LoginFragmentDirections
            .actionLoginFragmentToNavigationFragment( email, providerType )
        navController.navigate(action)
    }
    private fun createUser(email:String, providerType: ProviderType){
        val user = UserModel(email, authViewModel.getDisplayName())
        authViewModel.createUser(user) {
            if(it!=null){
                showHome(email, providerType)
            }else{
                authViewModel.logout(providerType)
                dialogManager.showAlert(
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
}