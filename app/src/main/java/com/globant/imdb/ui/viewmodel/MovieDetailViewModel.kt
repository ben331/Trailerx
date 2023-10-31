package com.globant.imdb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.domain.movies.GetMovieByIdUseCase
import com.globant.imdb.domain.movies.GetOfficialTrailerUseCase
import kotlinx.coroutines.launch

class MovieDetailViewModel: ViewModel() {

    // Live data
    val currentMovie = MutableLiveData<MovieDetail>()
    val videoIframe = MutableLiveData<String?>()

    // Use Cases
    val getMovieByIdUseCase = GetMovieByIdUseCase()
    val getTrailerUseCase = GetOfficialTrailerUseCase()

    fun onCreate(movieId:Int){
        viewModelScope.launch {
            val result = getMovieByIdUseCase(movieId)
            result?.let {
                currentMovie.postValue(it)
            }
        }
        viewModelScope.launch {
            val result = getTrailerUseCase(movieId, true)
            result?.let {
                videoIframe.postValue(result)
            }
        }
    }
}