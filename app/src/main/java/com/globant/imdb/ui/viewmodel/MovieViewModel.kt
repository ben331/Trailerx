package com.globant.imdb.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.model.Movie
import com.globant.imdb.domain.GetTopRatedMoviesUseCase
import kotlinx.coroutines.launch

class MovieViewModel: ViewModel() {

    private val mainMovie = MutableLiveData<Movie>()

    val getTopRatedMoviesUseCase = GetTopRatedMoviesUseCase()

    fun onCreate() {
        viewModelScope.launch {
            val result = getTopRatedMoviesUseCase()
            if(result.isNotEmpty()){
                mainMovie.postValue(result[0])
            }
        }
    }
}