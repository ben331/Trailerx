package tech.benhack.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.AndroidEntryPoint
import tech.benhack.common.Constants
import tech.benhack.home.databinding.FragmentHomeBinding
import tech.benhack.home.model.SectionHomeItem
import tech.benhack.home.view.components.MovieHomeListener
import tech.benhack.home.view.screens.HomeScreen
import tech.benhack.home.viewmodel.HomeViewModel
import tech.benhack.ui.helpers.DialogManager
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

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val homeViewModel: HomeViewModel by viewModels()

    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.homeComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val movie by homeViewModel.mainMovie.observeAsState(initial = null)
                val nowPlayingMovies by homeViewModel.nowPlayingMovies.observeAsState(initial = emptyList())
                val upcomingMovies by homeViewModel.upcomingMovies.observeAsState(initial = emptyList())
                val popularMovies by homeViewModel.popularMovies.observeAsState(initial = emptyList())

                TrailerxTheme {
                    HomeScreen(
                        mainMovieTitle = movie?.title ?: "----",
                        mainImageUrl = Constants.IMAGES_BASE_URL + movie?.backdropPath,
                        mainVideoUrl = "",
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

    override fun onResume() {
        super.onResume()
        binding.homeComposeView.disposeComposition()
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