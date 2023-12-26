package com.globant.imdb.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.data.network.firebase.ProviderType
import com.globant.imdb.di.IoDispatcher
import com.globant.imdb.domain.moviesUseCases.GetNowPlayingMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetPopularMoviesUseCase
import com.globant.imdb.domain.moviesUseCases.GetUpcomingMoviesUseCase
import com.globant.imdb.domain.userUseCases.GetUserMoviesUseCase
import com.globant.imdb.ui.helpers.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val getUserMoviesUseCase: GetUserMoviesUseCase,
    private val imageLoader: ImageLoader,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    val isImagesLoading = MutableLiveData(false)

    fun setupName(useName: (remoteDisplayName:String?) -> Unit) {
        authManager.setupName(useName)
    }

    fun logout(provider: ProviderType){
        authManager.logout(provider)
    }

    fun preloadUserDataAndImages(context:Context, callback: () -> Unit) {
        isImagesLoading.postValue(true)
        viewModelScope.launch(ioDispatcher){
            val nowPlayingMovies = getNowPlayingMoviesUseCase()
            val upcomingMovies = getUpcomingMovies()
            val popularMovies = getPopularMoviesUseCase()
            val watchList = getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES)
                .toRightValueOrNull() ?: emptyList()
            val history = getUserMoviesUseCase(CategoryType.HISTORY_MOVIES)
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