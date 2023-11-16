package com.globant.imdb.ui.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.core.DialogManager
import com.globant.imdb.data.model.user.User
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.data.remote.firebase.ProviderType
import com.globant.imdb.domain.user.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val createUserUseCase:CreateUserUseCase,
    private val authManager: FirebaseAuthManager,
    private val dialogManager: DialogManager
): ViewModel() {
    val isLoading = MutableLiveData(false)

    private fun handleFailure(title:Int, msg:Int){
        isLoading.postValue(false)
        dialogManager.showAlert(title, msg)
    }

    fun createUser(
        localUser: User,
        handleSuccess:(user: User?)->Unit
    ){
        createUserUseCase(localUser, handleSuccess, ::handleFailure)
    }

    fun getDisplayName():String{
        return authManager.getDisplayName()
    }

    fun setupName(useName: (remoteDisplayName:String?) -> Unit) {
        authManager.setupName(useName)
    }

    fun logout(provider:ProviderType){
        authManager.logout(provider)
    }

    fun signUpWithEmailAndPassword(
        email:String,
        password:String,
        displayName:String,
        onSuccess: (email:String)->Unit,
    ){
        authManager.signUpWithEmailAndPassword(
            email,
            password,
            displayName,
            onSuccess,
            ::handleFailure
        )
    }

    fun loginWithEmailAndPassword(
        email:String,
        password:String,
        onSuccess: (email:String, provides: ProviderType)->Unit,
    ){
        authManager.loginWithEmailAndPassword(
            email,
            password,
            onSuccess,
            ::handleFailure
        )
    }

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: ()->Unit,
    ){
        authManager.sendPasswordResetEmail(email, onSuccess, ::handleFailure)
    }

    fun loginWithApple(
        activity:Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
    ){
        authManager.loginWithApple(activity, onSuccess, ::handleFailure)
    }

    fun loginWithFacebook(
        activity:Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
    ){
        authManager.loginWithFacebook(activity, onSuccess, ::handleFailure)
    }

    fun loginWithGoogle(activity:Activity): Intent {
        return authManager.loginWithGoogle(activity)
    }

    fun onGoogleResult(
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
    ){
        authManager.onGoogleResult(intent, onSuccess, ::handleFailure)
    }

}