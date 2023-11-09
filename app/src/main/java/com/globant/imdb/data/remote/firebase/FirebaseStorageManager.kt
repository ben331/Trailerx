package com.globant.imdb.data.remote.firebase

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

private const val PROFILE_PATH = "images/profiles/"

class FirebaseStorageManager {

    private val storage: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    fun uploadPhoto(
        path:String,
        handleSuccess: () -> Unit,
        handleFailure: () -> Unit
    ){
        val file = Uri.fromFile(File(path))
        val photoRef = storage.child("$PROFILE_PATH${file.lastPathSegment}")
        val uploadTask = photoRef.putFile(file)

        uploadTask.addOnFailureListener {
            handleFailure()
        }.addOnSuccessListener { _ ->
            authManager.updateProfilePhotoURL(photoRef.downloadUrl.result, handleSuccess, handleFailure)
        }
    }

}