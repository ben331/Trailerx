package com.globant.imdb.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.core.DialogManager
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.model.movies.MovieConverter
import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.domain.movies.GetMovieByIdUseCase
import com.globant.imdb.domain.movies.GetOfficialTrailerUseCase
import com.globant.imdb.domain.user.AddMovieToListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val dialogManager: DialogManager,
    private val getMovieByIdUseCase:GetMovieByIdUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val addMovieToListUseCase:AddMovieToListUseCase
): ViewModel() {

    val isLoading = MutableLiveData(false)
    val currentMovie = MutableLiveData<MovieDetail>()
    val videoIframe = MutableLiveData<String?>()

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

    fun addMovieToWatchList(handleSuccess:(movie: Movie)->Unit){
        isLoading.postValue(true)
        currentMovie.value?.let {
            val movie = MovieConverter.movieDetailToMovie(it)
            addMovieToListUseCase(movie, 1, handleSuccess, ::handleFailure)
        }
    }

    fun recordHistory(){
        isLoading.postValue(true)
        currentMovie.value?.let {
            val movie = MovieConverter.movieDetailToMovie(it)
            addMovieToListUseCase(
                movie, 2,
                { Log.i("INFO", "Movie ${movie.title}, id:${movie.id} saved in history") },
                ::handleFailure)
        }
    }

    private fun handleFailure(title:Int, msg:Int){
        isLoading.postValue(false)
        dialogManager.showAlert(title, msg)
    }
}