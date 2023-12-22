package com.globant.imdb.ui.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.data.network.firebase.ProviderType
import com.globant.imdb.di.IoDispatcher
import com.globant.imdb.di.MainDispatcher
import com.globant.imdb.domain.userUseCases.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val createUserUseCase:CreateUserUseCase,
    private val authManager: FirebaseAuthManager,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {
    val isLoading = MutableLiveData(false)

    fun createUser(
        localUser: UserModel,
        handleResult:(user: UserModel?)->Unit
    ){
        viewModelScope.launch(ioDispatcher){
            val user = createUserUseCase(localUser)
            withContext(mainDispatcher){ handleResult(user) }
        }
    }

    fun getDisplayName():String{
        return authManager.getDisplayName()
    }

    fun logout(provider:ProviderType){
        authManager.logout(provider)
    }

    fun signUpWithEmailAndPassword(
        email:String,
        password:String,
        displayName:String,
        onSuccess: (email:String)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authManager.signUpWithEmailAndPassword(
            email,
            password,
            displayName,
            onSuccess,
            handleFailure
        )
    }

    fun loginWithEmailAndPassword(
        email:String,
        password:String,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authManager.loginWithEmailAndPassword(
            email,
            password,
            onSuccess,
            handleFailure
        )
    }

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: ()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authManager.sendPasswordResetEmail(email, onSuccess, handleFailure)
    }

    fun loginWithApple(
        activity:Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authManager.loginWithApple(activity, onSuccess, handleFailure)
    }

    fun loginWithFacebook(
        activity:Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authManager.loginWithFacebook(activity, onSuccess, handleFailure)
    }

    fun loginWithGoogle(activity:Activity): Intent {
        return authManager.loginWithGoogle(activity)
    }

    fun onGoogleResult(
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authManager.onGoogleResult(intent, onSuccess, handleFailure)
    }

}