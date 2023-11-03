package com.globant.imdb.ui.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.globant.imdb.databinding.FragmentProfileBinding
import com.globant.imdb.R
import com.globant.imdb.ui.view.adapters.MovieProfileAdapter
import com.globant.imdb.ui.view.adapters.MovieProfileViewHolder
import com.globant.imdb.ui.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment(), MovieProfileAdapter.ImageRenderListener, MovieProfileViewHolder.MovieListener {

    private val profileViewModel:ProfileViewModel by viewModels()

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    private lateinit var watchListAdapter: MovieProfileAdapter
    private lateinit var recentMoviesAdapter: MovieProfileAdapter
    private lateinit var favoritePeopleAdapter: MovieProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.setHandleFailure(::showAlert)
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
        setupRecyclerViews()
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.refresh(requireContext())
    }
    private fun setupRecyclerViews(){
        watchListAdapter = MovieProfileAdapter()
        recentMoviesAdapter = MovieProfileAdapter()
        favoritePeopleAdapter = MovieProfileAdapter()

        watchListAdapter.listNumber = 1
        recentMoviesAdapter.listNumber = 2
        favoritePeopleAdapter.listNumber = 3

        watchListAdapter.moviesListener = this
        recentMoviesAdapter.moviesListener = this
        favoritePeopleAdapter.moviesListener = this

        with(binding.listMoviesOne){
            titleContainer.sectionTitle.text = getString(R.string.watch_list)
            listDescription.text = getString(R.string.create_watch_list)
            btnActionList.text = getString(R.string.start_watch_list)

            moviesRecyclerView.adapter = watchListAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

        with(binding.listMoviesTwo){
            titleContainer.sectionTitle.text = getString(R.string.recently_viewed)
            listDescription.text = getString(R.string.content_recently_viewed)

            moviesRecyclerView.adapter = recentMoviesAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }

        with(binding.listMoviesThree){
            titleContainer.sectionTitle.text = getString(R.string.favorite_people)
            listDescription.text = getString(R.string.content_favorite_people)

            moviesRecyclerView.adapter = favoritePeopleAdapter
            moviesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            moviesRecyclerView.setHasFixedSize(true)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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

        profileViewModel.watchList.observe(viewLifecycleOwner){ movies ->
            if(movies.isEmpty()){
                with(binding.listMoviesOne) {
                    listDescription.visibility = View.VISIBLE
                    btnActionList.visibility = View.VISIBLE
                }
            }else{
                with(binding.listMoviesOne) {
                    listDescription.visibility = View.GONE
                    btnActionList.visibility = View.GONE
                }
            }
            watchListAdapter.movieList = movies
            watchListAdapter.notifyDataSetChanged()
        }

        profileViewModel.recentViewed.observe(viewLifecycleOwner){ movies ->
            if(movies.isEmpty()){
                binding.listMoviesTwo.listDescription.visibility = View.VISIBLE
            }else{
                binding.listMoviesTwo.listDescription.visibility = View.GONE
            }
            recentMoviesAdapter.movieList = movies
            recentMoviesAdapter.notifyDataSetChanged()
        }

        profileViewModel.favoritePeople.observe(viewLifecycleOwner){ movies ->
            if(movies.isEmpty()){
                binding.listMoviesThree.listDescription.visibility = View.VISIBLE
            }else{
                binding.listMoviesThree.listDescription.visibility = View.GONE
            }
            favoritePeopleAdapter.movieList = movies
            favoritePeopleAdapter.notifyDataSetChanged()
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
        profileViewModel.isLoading.postValue(false)
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
        val action = ProfileFragmentDirections.actionProfileFragmentToMovieFragment(id)
        navController.navigate(action)
    }

    override fun deleteFromList(id: Int, listNumber: Int) {
        profileViewModel.deleteMovieFromList(requireContext(), id, listNumber)
    }
}