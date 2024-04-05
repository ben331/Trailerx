package tech.benhack.home.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import tech.benhack.common.Constants
import tech.benhack.home.R
import tech.benhack.home.databinding.FragmentHomeBinding
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.helpers.ImageLoader
import tech.benhack.home.view.adapters.MovieAdapter
import tech.benhack.home.view.adapters.MovieViewHolder
import tech.benhack.home.viewmodel.HomeViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), MovieAdapter.ImageRenderListener, MovieViewHolder.MovieListener {

    companion object {
        private const val FIRST_HOME_TITLE = "first_home_title"
        private const val  SECOND_HOME_TITLE= "second_home_title"
        private const val  THIRD_HOME_TITLE= "third_home_title"
    }

    @Inject
    lateinit var dialogManager: DialogManager

    @Inject
    lateinit var imageLoader: ImageLoader

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var nowPlayingMoviesAdapter: MovieAdapter
    private lateinit var upcomingMoviesAdapter: MovieAdapter
    private lateinit var popularMoviesAdapter: MovieAdapter

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup
        setupButtons()
        setupLiveData()
        setupRecyclerViews()

        refresh()
    }

    private fun setupButtons(){
        binding.refreshLayout.setOnRefreshListener(::refresh)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupLiveData(){
        homeViewModel.isVideoAvailable.observe(viewLifecycleOwner, ::turnOfflineMode)

        homeViewModel.mainMovie.observe(viewLifecycleOwner) { currentMovie ->
            with(binding.mainTrailerContainer) {
                trailerName.text = currentMovie.title
                val imageUrl = Constants.IMAGES_BASE_URL + currentMovie.backdropPath
                imageLoader.renderImageCenterCrop(requireContext(), imageUrl, trailerImageView)
                imageLoader.renderImageCenterCrop(requireContext(), imageUrl, imgWebView)
                homeViewModel.getTrailerOfMovie(currentMovie.id)
            }
        }

        homeViewModel.videoIframe.observe(viewLifecycleOwner) { videoIframe ->
            videoIframe?.let {
                with(binding.mainTrailerContainer.trailerWebView){
                    homeViewModel.isLoading.postValue(false)
                    if(homeViewModel.isVideoAvailable.value == true){
                        loadData(it, "text/html", "utf-8")
                        settings.javaScriptEnabled = true
                        webChromeClient = WebChromeClient()
                    }
                }
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = it
        }

        homeViewModel.nowPlayingMovies.observe(viewLifecycleOwner){
            nowPlayingMoviesAdapter.movieList = it
            nowPlayingMoviesAdapter.notifyDataSetChanged()
        }
        homeViewModel.upcomingMovies.observe(viewLifecycleOwner){
            upcomingMoviesAdapter.movieList = it
            upcomingMoviesAdapter.notifyDataSetChanged()
        }
        homeViewModel.popularMovies.observe(viewLifecycleOwner){
            popularMoviesAdapter.movieList = it
            popularMoviesAdapter.notifyDataSetChanged()
        }
    }

    private fun turnOfflineMode(isServiceAvailable: Boolean) {
        with(binding.mainTrailerContainer){
            if(isServiceAvailable){
                trailerWebView.visibility = View.VISIBLE
                if(homeViewModel.onlineMode.value == false){
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.connection_recovered),
                        Toast.LENGTH_SHORT
                    ).show()
                    homeViewModel.onlineMode.postValue(true)
                }
            } else {
                trailerWebView.visibility = View.GONE
                if(homeViewModel.onlineMode.value == true){
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.turn_offline_mode),
                        Toast.LENGTH_SHORT
                    ).show()
                    homeViewModel.onlineMode.postValue(false)
                }
            }
        }
    }

    private fun setupRecyclerViews(){
        nowPlayingMoviesAdapter = MovieAdapter()
        upcomingMoviesAdapter = MovieAdapter()
        popularMoviesAdapter = MovieAdapter()

        nowPlayingMoviesAdapter.numberList = 1
        upcomingMoviesAdapter.numberList = 2
        popularMoviesAdapter.numberList = 3

        nowPlayingMoviesAdapter.moviesListener = this
        upcomingMoviesAdapter.moviesListener = this
        popularMoviesAdapter.moviesListener = this

        remoteConfig.fetchAndActivate().addOnCompleteListener { _ ->
            with(binding.listMoviesOne){
                titleContainer.sectionTitle.text = remoteConfig.getString(FIRST_HOME_TITLE)
                listDescription.visibility = View.GONE
                moviesRecyclerView.adapter = nowPlayingMoviesAdapter
                moviesRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                moviesRecyclerView.setHasFixedSize(true)
            }

            with(binding.listMoviesTwo){
                titleContainer.sectionTitle.text = remoteConfig.getString(SECOND_HOME_TITLE)
                listDescription.visibility = View.GONE
                moviesRecyclerView.adapter = upcomingMoviesAdapter
                moviesRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                moviesRecyclerView.setHasFixedSize(true)
            }

            with(binding.listMoviesTree){
                titleContainer.sectionTitle.text = remoteConfig.getString(THIRD_HOME_TITLE)
                listDescription.visibility = View.GONE
                moviesRecyclerView.adapter = popularMoviesAdapter
                moviesRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                moviesRecyclerView.setHasFixedSize(true)
            }
        }
    }

    private fun refresh() {
        homeViewModel.onCreate()
    }

    override fun renderImage(url: String, image: ImageView) {
        imageLoader.renderImageCenterCrop(requireContext(), url, image)
    }

    override fun showDetails(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(id)
        findNavController().navigate(action)
    }

    override fun addToList(id: Int) {
        if(homeViewModel.username.isNotEmpty()){
            homeViewModel.addMovieToWatchList(id, requireContext())
        }else{
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(action)
        }
    }


}