package com.globant.imdb.data.network.firebase

import android.net.Uri
import com.google.firebase.storage.StorageReference
import java.io.File
import javax.inject.Inject

private const val PROFILE_PATH = "images/profiles/"

class FirebaseStorageManager @Inject constructor(
    private val storage:StorageReference,
    private val authManager: FirebaseAuthManager,
) {
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