package com.globant.imdb.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.network.firebase.FirebaseAuthManager
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.userUseCases.DeleteMovieFromUserListUseCase
import com.globant.imdb.domain.userUseCases.GetUserMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val getUserMoviesUseCase:GetUserMoviesUseCase,
    private val deleteMovieFromUserListUseCase:DeleteMovieFromUserListUseCase,
): ViewModel() {

    val photoUri = MutableLiveData<Uri?>()
    val watchList = MutableLiveData<List<MovieItem>>()
    val recentViewed = MutableLiveData<List<MovieItem>>()
    val favoritePeople = MutableLiveData<List<MovieItem>>()
    val isLoading = MutableLiveData(false)
    val username:String by lazy { authManager.getEmail() }

    fun refresh(
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        if(username.isNotEmpty()){
            isLoading.postValue(true)
            val uri = authManager.getProfilePhotoURL()
            if(uri!=null){
                photoUri.postValue(uri)
            }

            getUserMoviesUseCase( CategoryType.WATCH_LIST_MOVIES, { movies ->
                watchList.postValue(movies)
                isLoading.postValue(false)
            }, handleFailure)

            getUserMoviesUseCase( CategoryType.HISTORY_MOVIES, { movies ->
                recentViewed.postValue(movies)
                isLoading.postValue(false)
            }, handleFailure)

            getUserMoviesUseCase( CategoryType.FAVORITE_PEOPLE, { movies ->
                favoritePeople.postValue(movies)
                isLoading.postValue(false)
            }, handleFailure)
        }
    }

    fun deleteMovieFromList(
        movieId:Int, listType:CategoryType,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        isLoading.postValue(true)
        deleteMovieFromUserListUseCase(movieId, listType,{
            refresh(handleFailure)
        }, handleFailure)
    }
}