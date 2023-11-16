package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.core.DialogManager
import com.globant.imdb.data.model.movies.MovieModel
import com.globant.imdb.domain.movies.GetNowPlayingMoviesUseCase
import com.globant.imdb.domain.movies.GetPopularMoviesUseCase
import com.globant.imdb.domain.movies.GetRandomTopMovieUseCase
import com.globant.imdb.domain.movies.GetOfficialTrailerUseCase
import com.globant.imdb.domain.movies.GetUpcomingMoviesUseCase
import com.globant.imdb.domain.user.AddMovieToListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dialogManager: DialogManager,
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getRandomTopMovieUseCase:GetRandomTopMovieUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val addMovieToListUseCase:AddMovieToListUseCase,
): ViewModel() {

    val mainMovie = MutableLiveData<MovieModel>()
    val videoIframe = MutableLiveData<String?>()
    val nowPlayingMovies = MutableLiveData<List<MovieModel>>()
    val upcomingMovies = MutableLiveData<List<MovieModel>>()
    val popularMovies = MutableLiveData<List<MovieModel>>()
    val isLoading = MutableLiveData(false)

    @SuppressLint("NotifyDataSetChanged")
    fun onCreate() {
        isLoading.postValue(true)
        val coroutineA = viewModelScope.launch {
            val result = getRandomTopMovieUseCase()
            result?.let {
                mainMovie.postValue(it)
            }
        }
        val coroutineB = viewModelScope.launch {
            val result = getNowPlayingMoviesUseCase()
            if(result.isNotEmpty()){
                nowPlayingMovies.postValue(result)
            }
        }
        val coroutineC = viewModelScope.launch {
            val result = getUpcomingMovies()
            if(result.isNotEmpty()){
                upcomingMovies.postValue(result)
            }
        }
        val coroutineD = viewModelScope.launch {
            val result = getPopularMoviesUseCase()
            if(result.isNotEmpty()){
                popularMovies.postValue(result)
            }
        }
        viewModelScope.launch {
            coroutineA.join()
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
        onSuccess:(MovieModel)->Unit
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
            addMovieToListUseCase(movie, 1, onSuccess){ title, msg ->
                isLoading.postValue(false)
                dialogManager.showAlert(title, msg)
            }
        }
    }
}