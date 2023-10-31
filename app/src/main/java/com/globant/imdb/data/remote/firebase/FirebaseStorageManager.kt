package com.globant.imdb.data.remote.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import com.globant.imdb.R

private const val PROFILE_PATH = "images/profiles/"

class FirebaseStorageManager {

    private val storageRef: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    fun uploadPhoto(
        context:Context,
        path:String,
        handleSuccess: ()->Unit,
        handleAlert: (title:String, msg:String) -> Unit,
    ){
        val file = Uri.fromFile(File(path))
        val email = authManager.getProfileEmail()
        val photoRef = storageRef.child("$PROFILE_PATH$email.png")
        val uploadTask = photoRef.putFile(file)

        uploadTask.addOnFailureListener {
            handleAlert(
                context.getString(R.string.error),
                context.getString(R.string.fail_new_photo)
            )
        }.addOnSuccessListener { _ ->
            authManager.updateProfilePhotoURL(context, photoRef.downloadUrl.result, handleSuccess, handleAlert)
        }
    }

}