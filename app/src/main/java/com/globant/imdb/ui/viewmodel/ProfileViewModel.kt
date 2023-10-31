package com.globant.imdb.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.remote.firebase.FirebaseAuthManager
import com.globant.imdb.domain.user.AddMovieToWatchListUseCase
import com.globant.imdb.domain.user.GetWatchListUseCase
import com.globant.imdb.domain.user.SetupUserRepositoryUseCase
import com.globant.imdb.ui.view.adapters.MovieAdapter

class ProfileViewModel: ViewModel() {

    // Live data
    val photoUri = MutableLiveData<Uri?>()

    private val setupUserRepositoryUseCase = SetupUserRepositoryUseCase()
    private val getWatchListUseCase = GetWatchListUseCase()
    private val addMovieToWatchListUseCase = AddMovieToWatchListUseCase()

    val watchList = MutableLiveData<List<Movie>>()
    val isLoading = MutableLiveData(false)

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    lateinit var adapter: MovieAdapter

    fun setupProfileRepository(
        handleFailure:(title:String, msg:String)->Unit
    ){
        setupUserRepositoryUseCase({ movies->
                watchList.postValue(movies)
                isLoading.postValue(false)
            },
            { movie ->
                watchList.postValue(watchList.value!!.plus(movie))
                adapter.notifyItemInserted(adapter.itemCount)
                isLoading.postValue(false)
            },
            handleFailure
        )
    }

    fun refresh(context:Context) {
        isLoading.postValue(true)
        val uri = authManager.getProfilePhotoURL()
        if(uri!=null){
            photoUri.postValue(uri)
        }
        getWatchListUseCase(context)
    }

    fun addMovieToWatchList(context:Context, movie:Movie){
        addMovieToWatchListUseCase(context, movie)
    }
}