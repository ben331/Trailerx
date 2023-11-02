package com.globant.imdb.ui.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.imdb.databinding.FragmentProfileBinding
import com.globant.imdb.R
import com.globant.imdb.ui.view.adapters.MovieAdapter
import com.globant.imdb.ui.view.adapters.MovieViewHolder
import com.globant.imdb.ui.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(), MovieAdapter.ImageRenderListener, MovieViewHolder.MovieListener {

    private val profileViewModel:ProfileViewModel by viewModels()

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private lateinit var watchListAdapter: MovieAdapter
    private lateinit var recentMoviesAdapter: MovieAdapter
    private lateinit var favoritePeopleAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveData()
        setupRecyclerViews()
        setupButtons()
        profileViewModel.refresh(requireContext())
    }

    private fun setupRecyclerViews(){
        watchListAdapter = MovieAdapter()
        watchListAdapter.movieList = profileViewModel.watchList
        watchListAdapter.moviesListener = this

        with(binding.listMoviesOne){
            titleContainer.sectionTitle.text = getString(R.string.watch_list)
            moviesRecyclerView.adapter = watchListAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

        with(binding.listMoviesTwo){
            titleContainer.sectionTitle.text = getString(R.string.section_upcoming)
            listDescription.visibility = View.GONE
        }

        with(binding.listMoviesThree){
            titleContainer.sectionTitle.text = getString(R.string.section_popular)
            listDescription.visibility = View.GONE
        }
    }

    private fun setupLiveData(){
        profileViewModel.photoUri.observe(viewLifecycleOwner){
            Picasso.with(requireContext())
                .load(it)
                .fit()
                .centerCrop()
                .into(binding.profileHeaderContainer.profilePhotoContainer.profileImage)
        }

        profileViewModel.isLoading.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = it
        }
    }

    private fun setupButtons(){
        binding.profileHeaderContainer.btnSettings.setOnClickListener(::showPopup)
        binding.refreshLayout.setOnRefreshListener {
            profileViewModel.refresh(requireContext())
        }
    }

    private fun showPopup(v: View) {
        val parent = parentFragment?.parentFragment as NavigationFragment
        val popup = PopupMenu(requireActivity(), v)
        popup.setOnMenuItemClickListener(parent)
        popup.inflate(R.menu.profile_popup_menu)
        popup.show()
    }

    private fun showAlert(title:String, message:String){
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.accept, null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun renderImage(url: String, image: ImageView) {
        Picasso.with(requireContext())
            .load(url)
            .fit()
            .centerCrop()
            .into(image)
    }

    override fun showDetails(id: Int) {
        TODO("Not yet implemented")
    }

    override fun addToWatchList(id: Int) {
        TODO("Not yet implemented")
    }
}