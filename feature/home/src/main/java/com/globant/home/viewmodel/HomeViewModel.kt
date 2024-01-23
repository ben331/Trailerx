package com.globant.home.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.auth.repository.AuthRepositoryImpl
import com.globant.common.CategoryType
import com.globant.home.R
import com.globant.common.di.IoDispatcher
import com.globant.common.di.MainDispatcher
import com.globant.movies.model.MovieItem
import com.globant.movies.usecase.AddMovieToUserListUseCase
import com.globant.movies.usecase.GetNowPlayingMoviesUseCase
import com.globant.movies.usecase.GetOfficialTrailerUseCase
import com.globant.movies.usecase.GetPopularMoviesUseCase
import com.globant.movies.usecase.GetRandomTopMovieUseCase
import com.globant.movies.usecase.GetUpcomingMoviesUseCase
import com.globant.movies.usecase.GetUserMoviesUseCase
import com.globant.ui.helpers.DialogManager
import com.globant.ui.helpers.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "Home viewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getRandomTopMovieUseCase:GetRandomTopMovieUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val addMovieToUserListUseCase:AddMovieToUserListUseCase,
    private val getUserMoviesUseCase: GetUserMoviesUseCase,
    private val dialogManager: DialogManager,
    private val imageLoader: ImageLoader,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    val mainMovie = MutableLiveData<MovieItem>()
    val videoIframe = MutableLiveData<String?>()
    val nowPlayingMovies = MutableLiveData<List<MovieItem>>()
    val upcomingMovies = MutableLiveData<List<MovieItem>>()
    val popularMovies = MutableLiveData<List<MovieItem>>()
    val isLoading = MutableLiveData(false)
    val isVideoAvailable = MutableLiveData(true)
    val onlineMode = MutableLiveData(true)
    val isImagesLoading = MutableLiveData(false)

    val username:String by lazy { authRepository.getEmail() }

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
            viewModelScope.launch(ioDispatcher){
                val isAdded = addMovieToUserListUseCase(it, CategoryType.WATCH_LIST_MOVIES, username)
                withContext(mainDispatcher){
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
            }
        }
    }
}