package com.globant.imdb.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.model.movies.MovieConverter
import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.domain.movies.GetMovieByIdUseCase
import com.globant.imdb.domain.movies.GetOfficialTrailerUseCase
import com.globant.imdb.domain.user.AddMovieToListUseCase
import kotlinx.coroutines.launch

class MovieViewModel: ViewModel() {

    // Live data
    val isLoading = MutableLiveData(false)
    val currentMovie = MutableLiveData<MovieDetail>()
    val videoIframe = MutableLiveData<String?>()

    // Use Cases
    val getMovieByIdUseCase = GetMovieByIdUseCase()
    val getTrailerUseCase = GetOfficialTrailerUseCase()
    val addMovieToListUseCase = AddMovieToListUseCase()

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

    fun addMovieToWatchList(context: Context, handleSuccess:(movie: Movie)->Unit){
        isLoading.postValue(true)
        currentMovie.value?.let {
            val movie = MovieConverter.movieDetailToMovie(it)
            addMovieToListUseCase(context, movie, 1, handleSuccess)
        }
    }

    fun recordHistory(context: Context){
        isLoading.postValue(true)
        currentMovie.value?.let {
            val movie = MovieConverter.movieDetailToMovie(it)
            addMovieToListUseCase(context, movie, 2) { }
        }
    }
}