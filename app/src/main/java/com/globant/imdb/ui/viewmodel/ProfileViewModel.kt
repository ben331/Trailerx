package com.globant.imdb.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.domain.user.AddMovieToWatchListUseCase
import com.globant.imdb.domain.user.GetWatchListUseCase
import com.globant.imdb.ui.view.adapters.MovieAdapter

class ProfileViewModel: ViewModel() {

    // Live data
    val photoUri = MutableLiveData<Uri?>()

    private val getWatchListUseCase = GetWatchListUseCase()
    private val addMovieToWatchListUseCase = AddMovieToWatchListUseCase()

    val watchList = MutableLiveData<List<Movie>>()
    val isLoading = MutableLiveData(false)

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    lateinit var adapter: MovieAdapter

    fun refresh(context:Context) {
        isLoading.postValue(true)
        val uri = authManager.getProfilePhotoURL()
        if(uri!=null){
            photoUri.postValue(uri)
        }
        getWatchListUseCase(context, ::onSuccessGetMovies)
    }

    fun addMovieToWatchList(context:Context, movie:Movie){
        addMovieToWatchListUseCase(context, movie, ::onSuccessAddMovie)
    }

    private fun onSuccessAddMovie(movie:Movie){
        watchList.postValue(watchList.value!!.plus(movie))
        adapter.notifyItemInserted(adapter.itemCount)
        isLoading.postValue(false)
    }

    private fun onSuccessGetMovies(movies:ArrayList<Movie>){
        watchList.postValue(movies)
        isLoading.postValue(false)
    }
}