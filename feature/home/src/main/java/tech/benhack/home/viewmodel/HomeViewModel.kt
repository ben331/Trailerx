package tech.benhack.home.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import tech.benhack.auth.repository.AuthRepositoryImpl
import tech.benhack.common.CategoryType
import tech.benhack.home.R
import tech.benhack.di.IoDispatcher
import tech.benhack.di.MainDispatcher
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.usecase.AddMovieToUserListUseCase
import tech.benhack.movies.usecase.GetNowPlayingMoviesUseCase
import tech.benhack.movies.usecase.GetOfficialTrailerUseCase
import tech.benhack.movies.usecase.GetPopularMoviesUseCase
import tech.benhack.movies.usecase.GetRandomTopMovieUseCase
import tech.benhack.movies.usecase.GetUpcomingMoviesUseCase
import tech.benhack.ui.helpers.DialogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val getNowPlayingMoviesUseCase:GetNowPlayingMoviesUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val getPopularMoviesUseCase:GetPopularMoviesUseCase,
    private val getRandomTopMovieUseCase:GetRandomTopMovieUseCase,
    private val getUpcomingMovies:GetUpcomingMoviesUseCase,
    private val addMovieToUserListUseCase:AddMovieToUserListUseCase,
    private val dialogManager: DialogManager,
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

    val username:String by lazy { authRepository.getEmail() }

    @SuppressLint("NotifyDataSetChanged")
    fun onRefresh() {
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

    fun addMovieToWatchList(movieId:Int, context:Context){
        isLoading.postValue(true)
        val a :List<MovieItem> = nowPlayingMovies.value ?: emptyList()
        val b :List<MovieItem> = upcomingMovies.value ?: emptyList()
        val c :List<MovieItem> = popularMovies.value ?: emptyList()

        val homeMovies = a + b + c

        homeMovies.find { it.id == movieId }?.let{
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
                            context.getString(R.string.error),
                            context.getString(R.string.server_error)
                        )
                    }
                    isLoading.postValue(false)
                }
            }
        }
    }
}