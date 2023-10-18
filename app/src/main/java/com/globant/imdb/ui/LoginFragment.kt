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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.globant.imdb.R
import com.globant.imdb.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

private val auth: FirebaseAuth by lazy {
    FirebaseAuth.getInstance()
}

class LoginFragment : Fragment() {

    private val binding:FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    private val navController:NavController by lazy {
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
        binding.btnLogin.setOnClickListener{

            hideKeyboard()

            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            auth.signInWithEmailAndPassword( email, password )
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val action = LoginFragmentDirections.actionLoginFragmentToNavigationFragment( email, null )
                        navController.navigate(action)
                    }else{
                        showAlert()
                    }
                }
        }

        binding.labelRegister.setOnClickListener{
            hideKeyboard()
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            navController.navigate(action)
        }

        setupWatcher()
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
                validateFields()
            }

        }

        binding.editTextEmail.addTextChangedListener(watcher)
        binding.editTextPassword.addTextChangedListener(watcher)
    }

    private fun validateFields(){
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        binding.btnLogin.isEnabled = ( email.isNotEmpty() && password.isNotEmpty() )
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}