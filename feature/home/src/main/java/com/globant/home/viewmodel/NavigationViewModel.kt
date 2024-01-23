package com.globant.home.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.auth.datasource.remote.ProviderType
import com.globant.auth.repository.AuthRepositoryImpl
import com.globant.common.CategoryType
import com.globant.common.di.IoDispatcher
import com.globant.movies.usecase.GetNowPlayingMoviesUseCase
import com.globant.movies.usecase.GetPopularMoviesUseCase
import com.globant.movies.usecase.GetUpcomingMoviesUseCase
import com.globant.movies.usecase.GetUserMoviesUseCase
import com.globant.ui.helpers.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val getUserMoviesUseCase: GetUserMoviesUseCase,
    private val imageLoader: ImageLoader,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    val isImagesLoading = MutableLiveData(false)

    private val username by lazy {
        authRepository.getEmail()
    }

    fun setupName(useName: (remoteDisplayName:String?) -> Unit) {
        authRepository.setupName(useName)
    }

    fun logout(provider: ProviderType){
        authRepository.logout(provider)
    }

    fun preloadUserDataAndImages(context:Context, callback: () -> Unit) {
        isImagesLoading.postValue(true)
        viewModelScope.launch(ioDispatcher){
            val nowPlayingMovies = getNowPlayingMoviesUseCase()
            val upcomingMovies = getUpcomingMovies()
            val popularMovies = getPopularMoviesUseCase()
            val watchList = getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES, username)
                .toRightValueOrNull() ?: emptyList()
            val history = getUserMoviesUseCase(CategoryType.HISTORY_MOVIES, username)
                .toRightValueOrNull() ?: emptyList()

            val allMovies =
                nowPlayingMovies    +
                upcomingMovies      +
                popularMovies       +
                watchList           +
                history

            imageLoader.preLoadImages(context, allMovies.map { it.backdropPath })
            isImagesLoading.postValue(false)
            callback()
        }
    }
}