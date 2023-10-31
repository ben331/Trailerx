package com.globant.imdb.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.data.remote.firebase.FirebaseStorageManager

class ProfileViewModel: ViewModel() {

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    private val storageManager: FirebaseStorageManager by lazy {
        FirebaseStorageManager()
    }

    val email:String? by lazy {
        authManager.getProfileEmail()
    }

    val photoUri = MutableLiveData<Uri?>()

    fun onCreate() {
        refreshPhoto()
    }

    fun uploadPhoto(
        context:Context,
        path:String,
        handleAlert: (title:String, msg:String) -> Unit
    ){
        storageManager.uploadPhoto(context, path, ::refreshPhoto, handleAlert)
    }

    private fun refreshPhoto(){
        val uri = authManager.getProfilePhotoURL()
        if(uri!=null){
            photoUri.postValue(uri)
        }
    }
}