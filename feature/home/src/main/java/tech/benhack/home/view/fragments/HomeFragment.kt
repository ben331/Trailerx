package tech.benhack.home.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import tech.benhack.common.Constants
import tech.benhack.home.R
import tech.benhack.home.databinding.FragmentHomeBinding
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.helpers.ImageLoader
import tech.benhack.home.viewmodel.HomeViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.AndroidEntryPoint
import tech.benhack.home.model.SectionHomeItem
import tech.benhack.home.view.components.MovieHomeListener
import tech.benhack.home.view.screens.HomeScreen
import tech.benhack.ui.theme.TrailerxTheme
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), MovieHomeListener {
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

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.homeComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val nowPlayingMovies by homeViewModel.nowPlayingMovies.observeAsState(initial = emptyList())
                val upcomingMovies by homeViewModel.upcomingMovies.observeAsState(initial = emptyList())
                val popularMovies by homeViewModel.popularMovies.observeAsState(initial = emptyList())

                TrailerxTheme {
                    HomeScreen(
                        sections = listOf(
                            SectionHomeItem(
                                title = remoteConfig.getString(FIRST_HOME_TITLE),
                                movies = nowPlayingMovies
                            ),
                            SectionHomeItem(
                                title = remoteConfig.getString(SECOND_HOME_TITLE),
                                movies = upcomingMovies
                            ),
                            SectionHomeItem(
                                title = remoteConfig.getString(THIRD_HOME_TITLE),
                                movies = popularMovies
                            ),
                        ),
                        listener = this@HomeFragment
                    )
                }
            }
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup
        setupButtons()
        setupLiveData()

        refresh()
    }

    override fun onResume() {
        super.onResume()
        binding.homeComposeView.disposeComposition()
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

    private fun refresh() {
        homeViewModel.onCreate()
    }

    override fun showDetails(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(id)
        findNavController().navigate(action)
    }

    override fun bookmarkAction(id: Int) {
        if(homeViewModel.username.isNotEmpty()){
            homeViewModel.addMovieToWatchList(id, requireContext())
        }else{
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(action)
        }
    }

    override fun showInfo(id: Int) { }
}