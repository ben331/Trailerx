package tech.benhack.auth.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import tech.benhack.auth.datasource.remote.ProviderType
import tech.benhack.auth.datasource.remote.response.UserModel
import tech.benhack.auth.repository.AuthRepositoryImpl
import tech.benhack.di.IoDispatcher
import tech.benhack.di.MainDispatcher
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
        handleResult:(user: Boolean)->Unit
    ){
        viewModelScope.launch(ioDispatcher){
            val user = authRepository.createUser(localUser)
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