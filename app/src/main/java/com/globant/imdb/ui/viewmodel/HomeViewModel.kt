package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.R
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.moviesUseCases.GetNowPlayingMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetPopularMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetRandomTopMovieUseCase
import com.globant.imdb.domain.moviesUseCases.GetOfficialTrailerUseCase
import com.globant.imdb.domain.moviesUseCases.GetUpcomingMoviesUseCase
import com.globant.imdb.domain.userUseCases.AddMovieToUserListUseCase
import com.globant.imdb.ui.helpers.DialogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getRandomTopMovieUseCase:GetRandomTopMovieUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val addMovieToUserListUseCase:AddMovieToUserListUseCase,
    private val dialogManager: DialogManager
): ViewModel() {

    val mainMovie = MutableLiveData<MovieItem>()
    val videoIframe = MutableLiveData<String?>()
    val nowPlayingMovies = MutableLiveData<List<MovieItem>>()
    val upcomingMovies = MutableLiveData<List<MovieItem>>()
    val popularMovies = MutableLiveData<List<MovieItem>>()
    val isLoading = MutableLiveData(false)
    val isVideoAvailable = MutableLiveData(true)
    val onlineMode = MutableLiveData(true)

    val username:String by lazy { authManager.getEmail() }

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
            if(result != null){
                videoIframe.postValue(result)
                isVideoAvailable.postValue(true)
            } else {
                isVideoAvailable.postValue(false)
            }
            isLoading.postValue(false)
        }
    }

    fun addMovieToWatchList(movieId:Int, numberList:Int, context:Context){
        isLoading.postValue(true)
        val homeMovies = when(numberList){
            1 -> nowPlayingMovies.value
            2 -> upcomingMovies.value
            3 -> popularMovies.value
            else -> emptyList()
        }
        homeMovies?.find { it.id == movieId }?.let{
            viewModelScope.launch { withContext(Dispatchers.IO){
                val isAdded = addMovieToUserListUseCase(it, CategoryType.WATCH_LIST_MOVIES)
                withContext(Dispatchers.Main){
                    if(isAdded){
                        dialogManager.showAlert(
                            context,
                            context.getString(R.string.success),
                            context.getString(R.string.success_movie_added, it.title)
                        )
                    }else{
                        dialogManager.showAlert(
                            context,
                            context.getString(R.string.success),
                            context.getString(R.string.server_error)
                        )
                    }
                    isLoading.postValue(false)
                }
            }}
        }
    }
}