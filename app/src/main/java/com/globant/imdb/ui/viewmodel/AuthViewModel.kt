package com.globant.imdb.ui.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import com.globant.imdb.data.model.user.User
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.data.remote.firebase.ProviderType
import com.globant.imdb.domain.user.CreateUserUseCase
import com.globant.imdb.domain.user.SetHandleFailureUseCase

class AuthViewModel: ViewModel() {
    val isLoading = MutableLiveData(false)

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    private val setHandleFailureUseCase = SetHandleFailureUseCase()
    private val createUserUseCase = CreateUserUseCase()
    fun createUser(
        context:Context,
        localUser: User,
        handleSuccess:(user: User?)->Unit
    ){
        createUserUseCase(context, localUser, handleSuccess)
    }

    fun getDisplayName():String{
        return authManager.getDisplayName()
    }

    fun getCallbackManager(): CallbackManager{
        return authManager.callbackManager
    }

    fun setupName(useName: (remoteDisplayName:String?) -> Unit) {
        authManager.setupName(useName)
    }

    fun setHandleFailure( handleFailure:(title:String, msg:String)->Unit ){
        setHandleFailureUseCase(handleFailure)
    }

    fun logout(provider:ProviderType){
        authManager.logout(provider)
    }

    fun signUpWithEmailAndPassword(
        context: Context,
        email:String,
        password:String,
        displayName:String,
        onSuccess: (email:String)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.signUpWithEmailAndPassword(context, email, password, displayName, onSuccess, onFailure)
    }

    fun loginWithEmailAndPassword(
        context: Context,
        email:String,
        password:String,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.loginWithEmailAndPassword(context, email, password, onSuccess, onFailure)
    }

    fun sendPasswordResetEmail(
        context: Context,
        email: String,
        onSuccess: ()->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.sendPasswordResetEmail(context, email, onSuccess, onFailure)
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
        context: Context,
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        onFailure:(title:String, msg:String)->Unit
    ){
        authManager.onGoogleResult(context, intent, onSuccess, onFailure)
    }

}