package com.globant.imdb.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.globant.imdb.R
import com.globant.imdb.data.remote.firebase.ProviderType
import com.globant.imdb.databinding.FragmentLoginBinding
import com.globant.imdb.ui.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    private val authViewModel:AuthViewModel by viewModels()

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
        setup()
    }

    private fun setup(){
        setupWatcher()

        binding.labelRegister.setOnClickListener{
            hideKeyboard()
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            navController.navigate(action)
        }

        binding.btnLogin.setOnClickListener{
            hideKeyboard()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            authViewModel.loginWithEmailAndPassword(email, password, ::showHome, ::showAlert)
        }

        binding.googleBtn.setOnClickListener {
            val googleIntent = authViewModel.loginWithGoogle(requireActivity())
            googleLauncher.launch(googleIntent)
        }

        binding.facebookBtn.setOnClickListener{
            authViewModel.loginWithFacebook(requireActivity(), ::showHome, ::showAlert)
        }

        binding.appleBtn.setOnClickListener{
            authViewModel.loginWithApple(requireActivity(), ::showHome, ::showAlert)
        }
    }

    private fun onGoogleResult(result:ActivityResult){
        if(result.resultCode == Activity.RESULT_OK){
            authViewModel.onGoogleResult(result.data, ::showHome, ::showAlert)
        }
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
                validateFields()
            }
        }
        binding.editTextEmail.addTextChangedListener(watcher)
        binding.editTextPassword.addTextChangedListener(watcher)
    }

    private fun validateFields(){
        val pattern = Patterns.EMAIL_ADDRESS
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        binding.btnLogin.isEnabled = (
                pattern.matcher(email).matches() && password.isNotEmpty() )
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

    private fun showHome(email:String, providerType: ProviderType){
        val action = LoginFragmentDirections
            .actionLoginFragmentToNavigationFragment( email, providerType )
        navController.navigate(action)
    }

    private fun showAlert(message:String){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.auth_error)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}