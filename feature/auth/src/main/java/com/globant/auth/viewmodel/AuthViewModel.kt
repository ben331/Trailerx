package com.globant.auth.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.auth.datasource.remote.ProviderType
import com.globant.auth.datasource.remote.response.UserModel
import com.globant.auth.di.IoDispatcher
import com.globant.auth.di.MainDispatcher
import com.globant.auth.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
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
        return authRepository.getDisplayName()
    }

    fun logout(provider:ProviderType){
        authRepository.logout(provider)
    }

    fun signUpWithEmailAndPassword(
        email:String,
        password:String,
        displayName:String,
        onSuccess: (email:String)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authRepository.signUpWithEmailAndPassword(
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
        authRepository.loginWithEmailAndPassword(
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
        authRepository.sendPasswordResetEmail(email, onSuccess, handleFailure)
    }

    fun loginWithApple(
        activity:Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authRepository.loginWithApple(activity, onSuccess, handleFailure)
    }

    fun loginWithFacebook(
        activity:Activity,
        onSuccess: (email:String, provides: ProviderType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authRepository.loginWithFacebook(activity, onSuccess, handleFailure)
    }

    fun loginWithGoogle(activity:Activity): Intent {
        return authRepository.loginWithGoogle(activity)
    }

    fun onGoogleResult(
        intent:Intent?,
        onSuccess: (email:String, provides:ProviderType)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        authRepository.onGoogleResult(intent, onSuccess, handleFailure)
    }

}