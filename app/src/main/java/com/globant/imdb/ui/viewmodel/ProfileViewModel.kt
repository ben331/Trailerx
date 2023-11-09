package com.globant.imdb.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.domain.user.DeleteMovieFromListUseCase
import com.globant.imdb.domain.user.GetUserMoviesUseCase
import com.globant.imdb.domain.user.SetHandleFailureUseCase

class ProfileViewModel: ViewModel() {

    // Live data
    val photoUri = MutableLiveData<Uri?>()

    private val getUserMoviesUseCase = GetUserMoviesUseCase()
    private val setHandleFailureUseCase = SetHandleFailureUseCase()
    private val deleteMovieFromListUseCase = DeleteMovieFromListUseCase()

    val watchList = MutableLiveData<List<Movie>>()
    val recentViewed = MutableLiveData<List<Movie>>()
    val favoritePeople = MutableLiveData<List<Movie>>()
    val isLoading = MutableLiveData(false)

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    fun refresh(context:Context) {
        isLoading.postValue(true)
        val uri = authManager.getProfilePhotoURL()
        if(uri!=null){
            photoUri.postValue(uri)
        }
        getUserMoviesUseCase(context, 1){ movies ->
            watchList.postValue(movies)
            isLoading.postValue(false)
        }
        getUserMoviesUseCase(context, 2){ movies ->
            recentViewed.postValue(movies)
            isLoading.postValue(false)
        }
        getUserMoviesUseCase(context, 3){ movies ->
            favoritePeople.postValue(movies)
            isLoading.postValue(false)
        }
    }

    fun setHandleFailure(handleAlert: (title:String, msg:String) -> Unit){
        setHandleFailureUseCase(handleAlert)
    }

    fun deleteMovieFromList(context: Context, movieId:Int, listNumber:Int){
        isLoading.postValue(true)
        deleteMovieFromListUseCase(context, movieId, listNumber){
            refresh(context)
        }
    }
}