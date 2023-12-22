package com.globant.imdb.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.R
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.di.IoDispatcher
import com.globant.imdb.di.MainDispatcher
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.userUseCases.DeleteMovieFromUserListUseCase
import com.globant.imdb.domain.userUseCases.GetUserMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val getUserMoviesUseCase:GetUserMoviesUseCase,
    private val deleteMovieFromUserListUseCase:DeleteMovieFromUserListUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    val photoUri = MutableLiveData<Uri?>()
    val watchList = MutableLiveData<List<MovieItem>?>()
    val recentViewed = MutableLiveData<List<MovieItem>?>()
    val favoritePeople = MutableLiveData<List<MovieItem>?>()
    val isLoading = MutableLiveData(false)
    val username:String by lazy { authManager.getEmail() }

    fun refresh() {
        if(username.isNotEmpty()){
            isLoading.postValue(true)
            val uri = authManager.getProfilePhotoURL()
            if(uri!=null){
                photoUri.postValue(uri)
            }

            viewModelScope.launch(ioDispatcher) {
                val movies = getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES)
                watchList.postValue(movies)
                isLoading.postValue(false)
            }

            viewModelScope.launch(ioDispatcher) {
                val movies = getUserMoviesUseCase(CategoryType.HISTORY_MOVIES)
                recentViewed.postValue(movies)
                isLoading.postValue(false)
            }

            viewModelScope.launch(ioDispatcher) {
                val movies = getUserMoviesUseCase(CategoryType.FAVORITE_PEOPLE)
                favoritePeople.postValue(movies)
                isLoading.postValue(false)
            }
        }
    }

    fun deleteMovieFromList(
        movieId:Int, listType:CategoryType,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            deleteMovieFromUserListUseCase(movieId, listType)
            withContext(mainDispatcher){
                handleFailure(R.string.error, R.string.delete_movie_error)
            }
        }
    }
}