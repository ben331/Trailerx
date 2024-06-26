package tech.benhack.auth.view

import android.app.Activity
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
import tech.benhack.auth.R
import tech.benhack.auth.databinding.FragmentSignUpBinding
import tech.benhack.auth.viewmodel.AuthViewModel
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.helpers.FormValidator
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup
        setupWatcher()
        setupLiveData()
        setupButtons()
        setupForm()
    }

    private fun setupLiveData(){
        authViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if(isLoading){
                binding.constraintLayout.isEnabled = false
                binding.progressComponent.visibility = View.VISIBLE
            }else{
                binding.constraintLayout.isEnabled = true
                binding.progressComponent.visibility = View.GONE
            }
        }
    }


    private fun setupButtons(){
        binding.backButton.setOnClickListener{
            navController.popBackStack()
        }

        binding.btnAccept.setOnClickListener {
            authViewModel.isLoading.postValue(true)
            hideKeyboard()

            val displayName = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            authViewModel.signUpWithEmailAndPassword(
                email,
                password,
                displayName,
                ::showLogin,
                ::handleFailure,
            )
        }
    }

    private fun setupForm(){
        with(binding.editTextName) {
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && !FormValidator.validateIsNotBlank( text.toString())) {
                    binding.textLayoutName.error = getString(R.string.required_field)
                    binding.textLayoutName.isErrorEnabled = true
                }else{
                    binding.textLayoutName.isErrorEnabled = false
                }
            }}

        with(binding.editTextEmail) {
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && !FormValidator.validateEmail( text.toString())) {
                    binding.textLayoutEmail.error = getString(R.string.invalid_email)
                    binding.textLayoutEmail.isErrorEnabled = true
                }else{
                    binding.textLayoutEmail.isErrorEnabled = false
                }
            }}

        with(binding.editTextPassword) {
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus && !FormValidator.validatePassword( text.toString())) {
                    binding.textLayoutPassword.error = getString(R.string.invalid_password)
                    binding.textLayoutPassword.isErrorEnabled = true
                }else{
                    binding.textLayoutPassword.isErrorEnabled = false
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
                if(FormValidator.validateSignUp(displayName, email, password)){
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