package com.globant.imdb.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager

class ProfileViewModel: ViewModel() {

    // Live data
    val photoUri = MutableLiveData<Uri?>()

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    fun onCreate() {
        val uri = authManager.getProfilePhotoURL()
        if(uri!=null){
            photoUri.postValue(uri)
        }
    }
}