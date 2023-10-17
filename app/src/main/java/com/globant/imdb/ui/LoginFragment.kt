package com.globant.imdb.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            val email = binding.editTextUser.text.toString()
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
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            navController.navigate(action)
        }
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