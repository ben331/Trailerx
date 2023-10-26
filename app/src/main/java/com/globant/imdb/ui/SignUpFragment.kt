package com.globant.imdb.ui

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.globant.imdb.R
import com.globant.imdb.data.local.room.model.UserValidator
import com.globant.imdb.data.remote.firebase.ProviderType
import com.globant.imdb.databinding.FragmentSignUpBinding
import com.globant.imdb.ui.viewmodel.AuthViewModel

class SignUpFragment : Fragment() {

    private val authViewModel:AuthViewModel by activityViewModels()

    private val binding:FragmentSignUpBinding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        findNavController()
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
        setupButtons()
        setupForm()
    }

    private fun setupButtons(){
        binding.backButton.setOnClickListener{
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            navController.navigate(action)
        }

        binding.btnAccept.setOnClickListener {
            hideKeyboard()

            val displayName = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            authViewModel.signUpWithEmailAndPassword(
                email,
                password,
                displayName,
                ::showHome,
                ::showAlert
            )
        }
    }

    private fun setupForm(){
        with(binding.editTextName) {
            setOnFocusChangeListener { _, hasFocus ->
                error = if (!hasFocus && !UserValidator.validateIsNotBlank( text.toString())) {
                    R.string.required_field.toString()
                }else{
                    null
                }
            }}

        with(binding.editTextEmail) {
            setOnFocusChangeListener { _, hasFocus ->
                error = if (!hasFocus && !UserValidator.validateEmail( text.toString())) {
                    R.string.invalid_email.toString()
                }else{
                    null
                }
            }}

        with(binding.editTextPassword) {
            setOnFocusChangeListener { _, hasFocus ->
                error = if (!hasFocus && !UserValidator.validatePassword( text.toString())) {
                    R.string.invalid_password.toString()
                }else{
                    null
                }
            }}
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentView = requireView()
        inputMethodManager.hideSoftInputFromWindow(currentView.windowToken, 0)
    }

    private fun setupWatcher(){

        val watcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                val displayName = binding.editTextName.text.toString()
                val email = binding.editTextEmail.text.toString()
                val password = binding.editTextPassword.text.toString()
                if(UserValidator.validateSignUp(displayName, email, password)){
                    binding.btnAccept.isEnabled = true
                    binding.editTextName.error = null
                    binding.editTextEmail.error = null
                    binding.editTextPassword.error = null
                }else{
                    binding.btnAccept.isEnabled = false
                }
            }
        }
        binding.editTextName.addTextChangedListener(watcher)
        binding.editTextEmail.addTextChangedListener(watcher)
        binding.editTextPassword.addTextChangedListener(watcher)
    }

    private fun showHome(email:String, provider:ProviderType){
        val action = SignUpFragmentDirections.actionSignUpFragmentToNavigationFragment( email, provider )
        navController.navigate(action)
    }

    private fun showAlert(msg:String){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.error)
        builder.setMessage(msg)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }

}