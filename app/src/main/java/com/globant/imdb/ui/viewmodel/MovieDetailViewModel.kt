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

class MovieDetailViewModel: ViewModel() {

    // Live data
    val isLoading = MutableLiveData(false)
    val currentMovie = MutableLiveData<MovieDetail>()
    val videoIframe = MutableLiveData<String?>()

    // Use Cases
    val getMovieByIdUseCase = GetMovieByIdUseCase()
    val getTrailerUseCase = GetOfficialTrailerUseCase()
    val addMovieToWatchListUseCase = AddMovieToListUseCase()

    fun onCreate(movieId:Int){
        val coroutineA = viewModelScope.launch {
            val result = getMovieByIdUseCase(movieId)
            result?.let {
                currentMovie.postValue(it)
            }
        }
        val coroutineB = viewModelScope.launch {
            val result = getTrailerUseCase(movieId, true)
            result?.let {
                videoIframe.postValue(result)
            }
        }
        viewModelScope.launch {
            coroutineA.join()
            coroutineB.join()
            isLoading.postValue(false)
        }
    }

    fun addMovieToWatchList(context: Context, handleSuccess:(movie: Movie)->Unit){
        currentMovie.value?.let {
            val movie = MovieConverter.movieDetailToMovie(it)
            addMovieToWatchListUseCase(context, movie, 1, handleSuccess)
        }
    }
}