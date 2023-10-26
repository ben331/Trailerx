package com.globant.imdb.ui.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.data.remote.firebase.ProviderType

class AuthViewModel: ViewModel() {

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
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
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(msg:String)->Unit
    ){
        authManager.signUpWithEmailAndPassword(email, password, displayName, onSuccess, onFailure)
    }

    fun loginWithEmailAndPassword(
        email:String,
        password:String,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(msg:String)->Unit
    ){
        authManager.loginWithEmailAndPassword(email, password, onSuccess, onFailure)
    }

    fun loginWithApple(
        activity: Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(msg:String)->Unit
    ){
        authManager.loginWithApple(activity, onSuccess, onFailure)
    }

    fun loginWithFacebook(
        activity: Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(msg:String)->Unit
    ){
        authManager.loginWithFacebook(activity, onSuccess, onFailure)
    }

    fun loginWithGoogle(activity: Activity): Intent {
        return authManager.loginWithGoogle(activity)
    }

    fun onGoogleResult(
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(msg:String)->Unit
    ){
        authManager.onGoogleResult(intent, onSuccess, onFailure)
    }

}