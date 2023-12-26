package com.globant.imdb.ui.viewmodel

import android.content.Context
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
import com.globant.imdb.ui.helpers.DialogManager
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
    private val dialogManager: DialogManager,
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

    fun refresh(context:Context) {
        if(username.isNotEmpty()){
            isLoading.postValue(true)
            val uri = authManager.getProfilePhotoURL()
            if(uri!=null){
                photoUri.postValue(uri)
            }

            viewModelScope.launch(ioDispatcher) {
                getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES).apply {
                    withContext(mainDispatcher){
                        fold({
                            dialogManager.showAlert(context, R.string.error, R.string.fetch_movies_error)
                        },{
                            watchList.postValue(it)
                        })
                    }
                }
                isLoading.postValue(false)
            }

            viewModelScope.launch(ioDispatcher) {
                getUserMoviesUseCase(CategoryType.HISTORY_MOVIES).apply {
                    withContext(mainDispatcher){
                        fold({
                            dialogManager.showAlert(context, R.string.error, R.string.fetch_movies_error)
                        },{
                            watchList.postValue(it)
                        })
                    }
                }
                isLoading.postValue(false)
            }

            viewModelScope.launch(ioDispatcher) {
                getUserMoviesUseCase(CategoryType.FAVORITE_PEOPLE).apply {
                    withContext(mainDispatcher){
                        fold({
                            dialogManager.showAlert(context, R.string.error, R.string.fetch_movies_error)
                        },{
                            watchList.postValue(it)
                        })
                    }
                }
                isLoading.postValue(false)
            }
        }
    }

    fun deleteMovieFromList(
        movieId:Int, listType:CategoryType,
        handleResult:(isDeleted:Boolean)->Unit
    ){
        isLoading.postValue(true)
        viewModelScope.launch(ioDispatcher) {
            deleteMovieFromUserListUseCase(movieId, listType).also { isDeleted ->
                withContext(mainDispatcher) { handleResult(isDeleted) }
            }
            isLoading.postValue(false)
        }
    }
}