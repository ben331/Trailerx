package tech.benhack.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import tech.benhack.auth.repository.AuthRepositoryImpl
import tech.benhack.common.CategoryType
import tech.benhack.home.R
import tech.benhack.di.IoDispatcher
import tech.benhack.di.MainDispatcher
import tech.benhack.movies.model.MovieDetailItem
import tech.benhack.movies.model.toSimple
import tech.benhack.movies.usecase.AddMovieToUserListUseCase
import tech.benhack.movies.usecase.GetMovieByIdUseCase
import tech.benhack.movies.usecase.GetOfficialTrailerUseCase
import tech.benhack.ui.helpers.DialogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "Movie"

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
    private val getMovieByIdUseCase:GetMovieByIdUseCase,
    private val getTrailerUseCase:GetOfficialTrailerUseCase,
    private val addMovieToUserListUseCase:AddMovieToUserListUseCase,
    private val dialogManager: DialogManager,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    val isLoading = MutableLiveData(false)
    val currentMovie = MutableLiveData<MovieDetailItem?>()
    val videoIframe = MutableLiveData<String?>()
    val isVideoAvailable = MutableLiveData(true)

    val username:String by lazy { authRepository.getEmail() }

    fun onRefresh(movieId:Int){
        viewModelScope.launch {
            val result = getMovieByIdUseCase(movieId)
            currentMovie.postValue(result)
            recordHistory(result)
        }
        viewModelScope.launch {
            val result = getTrailerUseCase(movieId, true)
            if(result != null){
                videoIframe.postValue(result)
            } else {
                isVideoAvailable.postValue(false)
            }
        }
    }

    fun addMovieToWatchList(context: Context){
        isLoading.postValue(true)
        currentMovie.value?.let {
            viewModelScope.launch(ioDispatcher) {
                val isAdded = addMovieToUserListUseCase(it.toSimple(), CategoryType.WATCH_LIST_MOVIES, username)
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

    private fun recordHistory(movie:MovieDetailItem?){
        isLoading.postValue(true)
        movie?.let { movieDetail ->
            viewModelScope.launch(ioDispatcher) {
                addMovieToUserListUseCase(movieDetail.toSimple(), CategoryType.HISTORY_MOVIES, username).also { isAdded ->
                    if(isAdded) Log.i(TAG, "Movie ${movieDetail.title}, id:${movieDetail.id} recorded in history")
                    else Log.e(TAG, "Movie ${movieDetail.title}, id:${movieDetail.id} not recorded in history")
                }
            }
        }
    }
}