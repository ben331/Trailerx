package com.globant.imdb.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.core.Constants
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.moviesUseCases.GetNowPlayingMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetPopularMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetRandomTopMovieUseCase
import com.globant.imdb.domain.moviesUseCases.GetOfficialTrailerUseCase
import com.globant.imdb.domain.moviesUseCases.GetUpcomingMoviesUseCase
import com.globant.imdb.domain.userUseCases.AddMovieToUserListUseCase
import com.globant.imdb.domain.userUseCases.GetUserMoviesUseCase
import com.globant.imdb.ui.helpers.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
    private val getUserMoviesUseCase: GetUserMoviesUseCase,
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
    fun onCreate(context: Context) {
        isLoading.postValue(true)
        val coroutineA = viewModelScope.launch {
            val result = getNowPlayingMoviesUseCase()
            if(result.isNotEmpty()){
                nowPlayingMovies.postValue(result)
                preLoadImages(result, context)
            }
        }
        val coroutineB = viewModelScope.launch {
            val result = getUpcomingMovies()
            if(result.isNotEmpty()){
                upcomingMovies.postValue(result)
                preLoadImages(result, context)
            }
        }
        val coroutineC = viewModelScope.launch {
            val result = getPopularMoviesUseCase()
            if(result.isNotEmpty()){
                popularMovies.postValue(result)
                preLoadImages(result, context)
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
        if(movie!=null) {
            addMovieToUserListUseCase(movie, CategoryType.WATCH_LIST_MOVIES, onSuccess, onFailure)
        }
    }
    
    fun preLoadProfileData(context: Context){
        if(onlineMode.value == true){
            ImageLoader.preLoadImage(context, authManager.getProfilePhotoURL().toString())
            getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES, {preLoadImages(it,context)},{_,_->})
            getUserMoviesUseCase(CategoryType.HISTORY_MOVIES, {preLoadImages(it,context)},{_,_->})
        }
    }

    private fun preLoadImages(movies:List<MovieItem>, context:Context) {
        if(onlineMode.value == true){
            isLoading.postValue(true)
            movies.forEach { movie ->
                val imageUrl = (Constants.IMAGES_BASE_URL + movie.backdropPath)
                ImageLoader.preLoadImage(context, imageUrl)
            }
            isLoading.postValue(false)
        }
    }
}