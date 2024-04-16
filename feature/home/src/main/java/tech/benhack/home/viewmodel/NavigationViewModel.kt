package tech.benhack.home.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import tech.benhack.auth.datasource.remote.ProviderType
import tech.benhack.auth.repository.AuthRepositoryImpl
import tech.benhack.common.CategoryType
import tech.benhack.di.IoDispatcher
import tech.benhack.movies.usecase.GetNowPlayingMoviesUseCase
import tech.benhack.movies.usecase.GetPopularMoviesUseCase
import tech.benhack.movies.usecase.GetUpcomingMoviesUseCase
import tech.benhack.movies.usecase.GetUserMoviesUseCase
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val getUserMoviesUseCase: GetUserMoviesUseCase,
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

    fun preloadUserData(context:Context, callback: () -> Unit) {
        isImagesLoading.postValue(true)
        viewModelScope.launch(ioDispatcher){
            getNowPlayingMoviesUseCase()
            getUpcomingMovies()
            getPopularMoviesUseCase()
            getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES, username)
            getUserMoviesUseCase(CategoryType.HISTORY_MOVIES, username)
            isImagesLoading.postValue(false)
            callback()
        }
    }
}