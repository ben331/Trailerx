package tech.benhack.home.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import tech.benhack.di.IoDispatcher
import tech.benhack.ui.helpers.NetworkState
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.usecase.IsServiceAvailableUseCase
import tech.benhack.movies.usecase.SearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase:SearchMovieUseCase,
    private val isServiceAvailableUseCase: IsServiceAvailableUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher

): ViewModel() {

    val resultMovies = MutableLiveData<List<MovieItem>>()

    private val _uiState:MutableStateFlow<NetworkState> = MutableStateFlow(NetworkState.Loading)
    val uiState:StateFlow<NetworkState> = _uiState

    init {
        viewModelScope.launch(ioDispatcher) {
            isServiceAvailableUseCase.isServiceAvailable
                .collect { isConnected ->
                    _uiState.value = if(isConnected) NetworkState.Online else NetworkState.Offline
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun search(
        query:String
    ) {
        viewModelScope.launch(ioDispatcher) {
            val result = searchMovieUseCase(query)
            if(result.isNotEmpty()){
                resultMovies.postValue(result)
            }
        }
    }
}