package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.moviesUseCases.GetNowPlayingMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetPopularMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetRandomTopMovieUseCase
import com.globant.imdb.domain.moviesUseCases.GetOfficialTrailerUseCase
import com.globant.imdb.domain.moviesUseCases.GetUpcomingMoviesUseCase
import com.globant.imdb.domain.userUseCases.AddMovieToUserListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getRandomTopMovieUseCase:GetRandomTopMovieUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val addMovieToUserListUseCase:AddMovieToUserListUseCase,
): ViewModel() {

    val mainMovie = MutableLiveData<MovieItem>()
    val videoIframe = MutableLiveData<String?>()
    val nowPlayingMovies = MutableLiveData<List<MovieItem>>()
    val upcomingMovies = MutableLiveData<List<MovieItem>>()
    val popularMovies = MutableLiveData<List<MovieItem>>()
    val isLoading = MutableLiveData(false)

    @SuppressLint("NotifyDataSetChanged")
    fun onCreate() {
        isLoading.postValue(true)
        val coroutineA = viewModelScope.launch {
            val result = getNowPlayingMoviesUseCase()
            if(result.isNotEmpty()){
                nowPlayingMovies.postValue(result)
            }
        }
        val coroutineB = viewModelScope.launch {
            val result = getUpcomingMovies()
            if(result.isNotEmpty()){
                upcomingMovies.postValue(result)
            }
        }
        val coroutineC = viewModelScope.launch {
            val result = getPopularMoviesUseCase()
            if(result.isNotEmpty()){
                popularMovies.postValue(result)
            }
        }
        viewModelScope.launch {
            coroutineA.join()

            val coroutineD = viewModelScope.launch {
                val result = getRandomTopMovieUseCase()
                result?.let {
                    mainMovie.postValue(it)
                }
            }

            coroutineB.join()
            coroutineC.join()
            coroutineD.join()
            isLoading.postValue(false)
        }
    }

    fun getTrailerOfMovie(movieId:Int){
        isLoading.postValue(true)
        viewModelScope.launch {
            val result = getTrailerUseCase(movieId, false)
            result?.let {
                videoIframe.postValue(result)
            }
        }
    }

    fun addMovieToWatchList(
        movieId:Int,
        numberList:Int,
        onSuccess:(MovieItem)->Unit,
        onFailure:(title:Int, msg:Int)->Unit
    ){
        isLoading.postValue(true)
        val homeMovies = when(numberList){
            1 -> nowPlayingMovies.value
            2 -> upcomingMovies.value
            3 -> popularMovies.value
            else -> emptyList()
        }
        val movie = homeMovies?.find {
            it.id == movieId
        }
        if(movie!=null){
            addMovieToUserListUseCase(movie, MovieListType.WATCH_LIST_MOVIES, onSuccess, onFailure)
        }
    }
}