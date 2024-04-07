package tech.benhack.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tech.benhack.home.databinding.FragmentSearchBinding
import tech.benhack.home.view.components.MovieResultListener
import tech.benhack.home.view.screens.SearchScreen
import tech.benhack.home.viewmodel.SearchViewModel
import tech.benhack.ui.helpers.NetworkState
import tech.benhack.ui.theme.TrailerxTheme

@AndroidEntryPoint
class SearchFragment : Fragment(), MovieResultListener {

    private val searchViewModel: SearchViewModel by viewModels()

    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.searchComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val resultMovies by searchViewModel.resultMovies.observeAsState(initial = emptyList())

                val uiState by produceState<NetworkState>(
                    initialValue = NetworkState.Loading,
                    key1 = viewLifecycleOwner,
                    key2 = searchViewModel
                ) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                            searchViewModel.uiState.collect{ value = it }
                        }
                    }
                }

                TrailerxTheme {
                    SearchScreen(
                        movies = resultMovies,
                        onQueryChange = searchViewModel::search,
                        uiState = uiState,
                        listener = this@SearchFragment
                    )
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.searchComposeView.disposeComposition()
    }

    override fun showDetails(id: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToMovieFragment(id)
        findNavController().navigate(action)
    }
}