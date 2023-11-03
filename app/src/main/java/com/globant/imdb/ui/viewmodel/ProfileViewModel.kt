package com.globant.imdb.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.domain.user.DeleteMovieFromListUseCase
import com.globant.imdb.domain.user.GetWatchListUseCase
import com.globant.imdb.domain.user.SetHandleFailureUseCase
import com.globant.imdb.ui.view.adapters.MovieProfileAdapter

class ProfileViewModel: ViewModel() {

    // Live data
    val photoUri = MutableLiveData<Uri?>()

    private val getWatchListUseCase = GetWatchListUseCase()
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
        getWatchListUseCase(context, ::onSuccessGetMovies)
    }

    fun setHandleFailure(handleAlert: (title:String, msg:String) -> Unit){
        setHandleFailureUseCase(handleAlert)
    }

    private fun onSuccessGetMovies(movies:List<Movie>){
        watchList.postValue(movies)
        isLoading.postValue(false)
    }

    fun deleteMovieFromList(context: Context, movieId:Int, listNumber:Int){
        isLoading.postValue(true)
        deleteMovieFromListUseCase(context, movieId, listNumber){
            refresh(context)
        }
    }
}