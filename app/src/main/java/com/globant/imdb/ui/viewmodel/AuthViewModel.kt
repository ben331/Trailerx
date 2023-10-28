package com.globant.imdb.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.data.remote.firebase.ProviderType

class AuthViewModel: ViewModel() {
    val isLoading = MutableLiveData(false)

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    fun getCallbackManager(): CallbackManager{
        return authManager.callbackManager
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
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.signUpWithEmailAndPassword(email, password, displayName, onSuccess, onFailure)
    }

    fun loginWithEmailAndPassword(
        email:String,
        password:String,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.loginWithEmailAndPassword(email, password, onSuccess, onFailure)
    }

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: ()->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.sendPasswordResetEmail(email, onSuccess, onFailure)
    }

    fun loginWithApple(
        activity: Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.loginWithApple(activity, onSuccess, onFailure)
    }

    fun loginWithFacebook(
        fragment: Fragment,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.loginWithFacebook(fragment, onSuccess, onFailure)
    }

    fun loginWithGoogle(context: Context): Intent {
        return authManager.loginWithGoogle(context)
    }

    fun onGoogleResult(
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.onGoogleResult(intent, onSuccess, onFailure)
    }

}