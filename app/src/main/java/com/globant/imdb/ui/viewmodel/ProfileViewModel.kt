package com.globant.imdb.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView.Adapter
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

    val watchList = MutableLiveData<ArrayList<Movie>>()

    private val authManager: FirebaseAuthManager by lazy {
        FirebaseAuthManager()
    }

    lateinit var adapter: MovieAdapter

    fun setupProfileRepository(
        context: Context,
        handleFailure:(title:String, msg:String)->Unit
    ){
        setupUserRepositoryUseCase(
            context,
            { movies-> watchList.postValue(movies) },
            { movie ->
                watchList.value?.add(movie)
                adapter.notifyItemInserted(adapter.itemCount)
            },
            handleFailure
        )
    }

    fun onCreate() {
        val uri = authManager.getProfilePhotoURL()
        if(uri!=null){
            photoUri.postValue(uri)
        }
        getWatchListUseCase()
    }

    fun addMovieToWatchList(movie:Movie){
        addMovieToWatchListUseCase(movie)
    }
}