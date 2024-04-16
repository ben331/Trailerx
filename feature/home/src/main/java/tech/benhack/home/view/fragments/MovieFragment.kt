package tech.benhack.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import tech.benhack.home.R
import tech.benhack.home.databinding.FragmentMovieBinding
import tech.benhack.home.view.screens.MovieScreen
import tech.benhack.home.viewmodel.MovieViewModel
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.theme.TrailerxTheme
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

    @Inject
    lateinit var dialogManager: DialogManager

    private val movieViewModel: MovieViewModel by viewModels()

    private val args: MovieFragmentArgs by navArgs()

    private val binding: FragmentMovieBinding by lazy {
        FragmentMovieBinding.inflate(layoutInflater)
    }

    private val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.movieComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val currentMovie by movieViewModel.currentMovie.observeAsState(initial = null)
                val isVideoAvailable by movieViewModel.isVideoAvailable.observeAsState(initial = true)
                val context = LocalContext.current

                TrailerxTheme {
                    currentMovie?.let{
                        MovieScreen(
                            movie = it,
                            offLineMode = !isVideoAvailable,
                            onNavigateBack = { navController.popBackStack() },
                            onAddToWatchList = { movieViewModel.addMovieToWatchList(context) }
                        )
                    }
                }
            }
            return binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup
        setupLiveData()
    }

    override fun onResume() {
        super.onResume()
        binding.movieComposeView.disposeComposition()
        movieViewModel.onRefresh(args.movieId)
    }

    private fun setupLiveData(){
        movieViewModel.currentMovie.observe(viewLifecycleOwner){ movieDetailItem ->

            if(movieDetailItem == null) {
                dialogManager.showAlert (
                    requireContext(),
                    getString(R.string.error),
                    getString(R.string.get_movie_error)
                )
                navController.popBackStack()
            }
        }
    }
}