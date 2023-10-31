package com.globant.imdb.ui.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.globant.imdb.databinding.FragmentProfileBinding
import com.globant.imdb.R
import com.globant.imdb.ui.viewmodel.MediaViewModel
import com.globant.imdb.ui.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private val profileViewModel:ProfileViewModel by viewModels()
    private val mediaViewModel:MediaViewModel by viewModels()

    //Launchers
    private val cameraPermissionLauncher: ActivityResultLauncher<String> by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission(), mediaViewModel::onCameraPermissionResult)
    }
    private val oldCameraPermissionLauncher:ActivityResultLauncher<Array<String>> by lazy {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), mediaViewModel::onOldCameraPermissionResult)
    }
    private val galleryPermissionLauncher: ActivityResultLauncher<String> by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission(), mediaViewModel::onGalleryPermissionResult)
    }
    private val mediaLauncher: ActivityResultLauncher<Intent> by lazy {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(), mediaViewModel::onMediaResult)
    }

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mediaViewModel.fragment = this
        mediaViewModel.cameraPermissionLauncher = cameraPermissionLauncher
        mediaViewModel.oldCameraPermissionLauncher = oldCameraPermissionLauncher
        mediaViewModel.galleryPermissionLauncher = galleryPermissionLauncher
        mediaViewModel.mediaLauncher = mediaLauncher
        mediaViewModel.handleAlert = ::showAlert
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveData()
        setupButtons()
        profileViewModel.onCreate()
    }

    private fun setupLiveData(){
        profileViewModel.photoUri.observe(viewLifecycleOwner){
            Picasso.with(requireContext())
                .load(it)
                .fit()
                .centerCrop()
                .into(binding.profileHeaderContainer.profilePhotoContainer.profileImage)
        }
        mediaViewModel.photoUri.observe(viewLifecycleOwner){ uri ->
            uri?.let {
                profileViewModel.uploadPhoto(requireContext(),it.toString(), ::showAlert)
            }
        }
    }

    private fun setupButtons(){
        binding.profileHeaderContainer.btnSettings
            .setOnClickListener(::showSettingsPopup)
        binding.profileHeaderContainer.profilePhotoContainer.profileImage
            .setOnClickListener(::showProfilePopup)
    }

    private fun showSettingsPopup(v: View) {
        val parent = parentFragment?.parentFragment as NavigationFragment
        val popup = PopupMenu(requireActivity(), v)
        popup.setOnMenuItemClickListener(parent)
        popup.inflate(R.menu.settings_popup_menu)
        popup.show()
    }

    private fun showProfilePopup(v: View) {
        val popup = PopupMenu(requireActivity(), v)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.profile_popup_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.item_camera -> {
                mediaViewModel.fileName = "photo_${profileViewModel.email}"
                mediaViewModel.requestCamera()
            }
            R.id.item_gallery -> mediaViewModel.requestGallery()
        }
        return true
    }
    private fun showAlert(title:String, message:String){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }
}