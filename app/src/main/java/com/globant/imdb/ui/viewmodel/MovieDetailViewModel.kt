package com.globant.imdb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.model.MovieDetail
import com.globant.imdb.domain.GetMovieByIdUseCase
import kotlinx.coroutines.launch

class MovieDetailViewModel: ViewModel() {

    // Live data
    val currentMovie = MutableLiveData<MovieDetail>()

    // Use Cases
    val getMovieByIdUseCase = GetMovieByIdUseCase()

    fun onCreate(movieId:Int){
        viewModelScope.launch {
            val result = getMovieByIdUseCase(movieId)
            result?.let {
                currentMovie.postValue(it)
            }
        }
    }
}