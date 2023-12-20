package com.globant.imdb.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.imdb.R
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.userUseCases.DeleteMovieFromUserListUseCase
import com.globant.imdb.domain.userUseCases.GetUserMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val getUserMoviesUseCase:GetUserMoviesUseCase,
    private val deleteMovieFromUserListUseCase:DeleteMovieFromUserListUseCase,
): ViewModel() {

    val photoUri = MutableLiveData<Uri?>()
    val watchList = MutableLiveData<List<MovieItem>?>()
    val recentViewed = MutableLiveData<List<MovieItem>?>()
    val favoritePeople = MutableLiveData<List<MovieItem>?>()
    val isLoading = MutableLiveData(false)
    val username:String by lazy { authManager.getEmail() }

    fun refresh(handleFailure:(title:Int, msg:Int)->Unit) {
        if(username.isNotEmpty()){
            isLoading.postValue(true)
            val uri = authManager.getProfilePhotoURL()
            if(uri!=null){
                photoUri.postValue(uri)
            }

            viewModelScope.launch { withContext(Dispatchers.IO) {
                val movies = getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES)
                if ( movies != null ) watchList.postValue(movies)
                else {
                    withContext(Dispatchers.Main){
                        handleFailure(R.string.error, R.string.fetch_movies_error)
                    }
                }
                isLoading.postValue(false)
            }}

            viewModelScope.launch { withContext(Dispatchers.IO) {
                val movies = getUserMoviesUseCase(CategoryType.HISTORY_MOVIES)
                if ( movies != null ) recentViewed.postValue(movies)
                else {
                    withContext(Dispatchers.Main){
                        handleFailure(R.string.error, R.string.fetch_movies_error)
                    }
                }
                isLoading.postValue(false)
            }}

            viewModelScope.launch { withContext(Dispatchers.IO) {
                val movies = getUserMoviesUseCase(CategoryType.FAVORITE_PEOPLE)
                if ( movies != null ) favoritePeople.postValue(movies)
                else {
                    withContext(Dispatchers.Main){
                        handleFailure(R.string.error, R.string.fetch_movies_error)
                    }
                }
                isLoading.postValue(false)
            }}
        }
    }

    fun deleteMovieFromList(
        movieId:Int, listType:CategoryType,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        isLoading.postValue(true)
        viewModelScope.launch { withContext(Dispatchers.IO) {
            deleteMovieFromUserListUseCase(movieId, listType)
            handleFailure(R.string.error, R.string.delete_movie_error)
        }}
    }
}