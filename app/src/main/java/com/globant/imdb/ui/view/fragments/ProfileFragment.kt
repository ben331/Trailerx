package com.globant.imdb.ui.view.fragments

import android.annotation.SuppressLint
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
import com.globant.imdb.ui.helpers.DialogManager
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.ui.view.adapters.MovieProfileAdapter
import com.globant.imdb.ui.view.adapters.MovieProfileViewHolder
import com.globant.imdb.ui.view.adapters.StatsAdapter
import com.globant.imdb.ui.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), MovieProfileAdapter.ImageRenderListener, MovieProfileViewHolder.MovieListener {

    private val profileViewModel:ProfileViewModel by viewModels()

    @Inject
    lateinit var dialogManager: DialogManager

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    private lateinit var statsAdapter: StatsAdapter

    private lateinit var watchListAdapter: MovieProfileAdapter
    private lateinit var recentMoviesAdapter: MovieProfileAdapter
    private lateinit var favoritePeopleAdapter: MovieProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveData()
        setupTopRecyclerView()
        setupRecyclerViews()
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.refresh(::handleFailure)
    }

    private fun setupTopRecyclerView(){
        statsAdapter = StatsAdapter()
        with(binding.profileHeaderContainer.shortcutsRecyclerView){
            adapter = statsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerViews(){
        watchListAdapter = MovieProfileAdapter()
        recentMoviesAdapter = MovieProfileAdapter()
        favoritePeopleAdapter = MovieProfileAdapter()

        watchListAdapter.listType = CategoryType.WATCH_LIST_MOVIES
        recentMoviesAdapter.listType = CategoryType.HISTORY_MOVIES
        favoritePeopleAdapter.listType = CategoryType.FAVORITE_PEOPLE

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
            profileViewModel.refresh(::handleFailure)
        }
    }

    private fun showPopup(v: View) {
        val parent = parentFragment?.parentFragment as NavigationFragment
        val popup = PopupMenu(requireActivity(), v)
        popup.setOnMenuItemClickListener(parent)
        popup.inflate(R.menu.profile_popup_menu)
        popup.show()
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

    override fun deleteFromList(id: Int, listType: CategoryType) {
        profileViewModel.deleteMovieFromList(id, listType, ::handleFailure)
    }

    private fun handleFailure(title:Int, msg:Int){
        profileViewModel.isLoading.postValue(false)
        dialogManager.showAlert(requireContext(),title, msg)
    }
}