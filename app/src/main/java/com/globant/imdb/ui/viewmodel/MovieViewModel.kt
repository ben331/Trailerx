package com.globant.imdb.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.R
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.di.IoDispatcher
import com.globant.imdb.di.MainDispatcher
import com.globant.imdb.domain.model.MovieDetailItem
import com.globant.imdb.domain.model.toSimple
import com.globant.imdb.domain.moviesUseCases.GetMovieByIdUseCase
import com.globant.imdb.domain.moviesUseCases.GetOfficialTrailerUseCase
import com.globant.imdb.domain.userUseCases.AddMovieToUserListUseCase
import com.globant.imdb.ui.helpers.DialogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "Movie"

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
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

    val username:String by lazy { authManager.getEmail() }

    fun onRefresh(movieId:Int){
        viewModelScope.launch {
            val result = getMovieByIdUseCase(movieId)
            currentMovie.postValue(result)
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
                val isAdded = addMovieToUserListUseCase(it.toSimple(), CategoryType.WATCH_LIST_MOVIES)
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

    fun recordHistory(){
        isLoading.postValue(true)
        currentMovie.value?.let { movieDetail ->
            viewModelScope.launch(ioDispatcher) {
                addMovieToUserListUseCase(movieDetail.toSimple(), CategoryType.HISTORY_MOVIES).let { isAdded ->
                    if(isAdded) Log.i(TAG, "Movie ${movieDetail.title}, id:${movieDetail.id} recorded in history")
                    else Log.e(TAG, "Movie ${movieDetail.title}, id:${movieDetail.id} not recorded in history")
                }
            }
        }
    }
}