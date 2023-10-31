package com.globant.imdb.ui.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.R
import java.io.File

class MediaViewModel: ViewModel() {
    companion object {
        private val OLD_CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        val GALLERY_PERMISSION = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    //Properties
    lateinit var fileName: String
    lateinit var fragment: Fragment
    lateinit var handleAlert: (title: String, msg: String) -> Unit
    lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    lateinit var oldCameraPermissionLauncher: ActivityResultLauncher<Array<String>>
    lateinit var galleryPermissionLauncher: ActivityResultLauncher<String>
    lateinit var mediaLauncher: ActivityResultLauncher<Intent>

    // Live data
    val photoUri = MutableLiveData<Uri?>()

    private val context:Context by lazy {
        fragment.requireContext()
    }


    fun requestCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(context, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED){
                openCamera()
            }else{
                cameraPermissionLauncher.launch(CAMERA_PERMISSION)
            }
        } else {
            val permissionsToRequestArray = OLD_CAMERA_PERMISSIONS.filter {
                ContextCompat.checkSelfPermission(fragment.requireContext(), it) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            if (permissionsToRequestArray.isEmpty()) {
                openCamera()
            } else {
                oldCameraPermissionLauncher.launch(OLD_CAMERA_PERMISSIONS)
            }
        }
    }

    fun requestGallery(){
        if(ContextCompat.checkSelfPermission(context, GALLERY_PERMISSION) == PackageManager.PERMISSION_GRANTED){
            openGallery()
        }else{
            galleryPermissionLauncher.launch(GALLERY_PERMISSION)
        }
    }

    fun onCameraPermissionResult(isGranted:Boolean) {
        if(isGranted){
            openCamera()
        }else{
            handleAlert(
                context.getString(R.string.error),
                context.getString(R.string.camera_denied)
            )
        }
    }

    fun onOldCameraPermissionResult(permissions:Map<String, Boolean>) {
        if (permissions.all { it.value }){
            openCamera()
        }else{
            handleAlert(
                context.getString(R.string.error),
                context.getString(R.string.camera_denied)
            )
        }
    }

    fun onGalleryPermissionResult(isGranted:Boolean) {
        if(isGranted){
            openGallery()
        }else{
            handleAlert(
                context.getString(R.string.error),
                context.getString(R.string.gallery_denied)
            )
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File("${context.getExternalFilesDir(null)}/${fileName}")
        val uri = FileProvider.getUriForFile(context, context.packageName, file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        mediaLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        mediaLauncher.launch(intent)
    }

    fun onMediaResult(result: ActivityResult?) {
        val path = result?.data?.data?.path
        if (
            result?.resultCode == Activity.RESULT_OK &&
            path!=null
        ) {
            Uri.parse(path)?.let { uri ->
                photoUri.postValue(uri)
            }
        }else{
            handleAlert(
                context.getString(R.string.error),
                context.getString(R.string.fail_new_photo)
            )
        }
    }

}