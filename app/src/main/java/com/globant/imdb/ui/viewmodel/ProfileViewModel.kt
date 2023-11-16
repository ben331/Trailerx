package com.globant.imdb.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.core.DialogManager
import com.globant.imdb.data.model.movies.MovieModel
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.domain.user.DeleteMovieFromListUseCase
import com.globant.imdb.domain.user.GetUserMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dialogManager: DialogManager,
    private val authManager: FirebaseAuthManager,
    private val getUserMoviesUseCase:GetUserMoviesUseCase,
    private val deleteMovieFromListUseCase:DeleteMovieFromListUseCase,
): ViewModel() {

    val photoUri = MutableLiveData<Uri?>()
    val watchList = MutableLiveData<List<MovieModel>>()
    val recentViewed = MutableLiveData<List<MovieModel>>()
    val favoritePeople = MutableLiveData<List<MovieModel>>()
    val isLoading = MutableLiveData(false)

    private fun handleFailure(title:Int, msg:Int){
        isLoading.postValue(false)
        dialogManager.showAlert(title, msg)
    }

    fun refresh() {
        isLoading.postValue(true)
        val uri = authManager.getProfilePhotoURL()
        if(uri!=null){
            photoUri.postValue(uri)
        }

        getUserMoviesUseCase(1,{ movies ->
            watchList.postValue(movies)
            isLoading.postValue(false)
        }, ::handleFailure)

        getUserMoviesUseCase(2,{ movies ->
            recentViewed.postValue(movies)
            isLoading.postValue(false)
        }, ::handleFailure)

        getUserMoviesUseCase(3,{ movies ->
            favoritePeople.postValue(movies)
            isLoading.postValue(false)
        }, ::handleFailure)
    }

    fun deleteMovieFromList(movieId:Int, listNumber:Int){
        isLoading.postValue(true)
        deleteMovieFromListUseCase(movieId, listNumber,{
            refresh()
        }, ::handleFailure)
    }
}