package tech.benhack.home.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import tech.benhack.auth.repository.AuthRepositoryImpl
import tech.benhack.common.CategoryType
import tech.benhack.di.IoDispatcher
import tech.benhack.di.MainDispatcher
import tech.benhack.home.R
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.usecase.DeleteMovieFromUserListUseCase
import tech.benhack.movies.usecase.GetUserMoviesUseCase
import tech.benhack.ui.helpers.DialogManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepositoryImpl,
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
    val username:String by lazy { authRepository.getEmail() }

    fun refresh(context:Context) {
        if(username.isNotEmpty()){
            isLoading.postValue(true)
            val uri = authRepository.getProfilePhotoURL()
            if(uri!=null){
                photoUri.postValue(uri)
            }

            viewModelScope.launch(ioDispatcher) {
                getUserMoviesUseCase(CategoryType.WATCH_LIST_MOVIES, username).apply {
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
                getUserMoviesUseCase(CategoryType.HISTORY_MOVIES, username).apply {
                    withContext(mainDispatcher){
                        fold({
                            dialogManager.showAlert(context, R.string.error, R.string.fetch_movies_error)
                        },{
                            recentViewed.postValue(it)
                        })
                    }
                }
                isLoading.postValue(false)
            }

            viewModelScope.launch(ioDispatcher) {
                getUserMoviesUseCase(CategoryType.FAVORITE_PEOPLE, username).apply {
                    withContext(mainDispatcher){
                        fold({
                            dialogManager.showAlert(context, R.string.error, R.string.fetch_movies_error)
                        },{
                            favoritePeople.postValue(it)
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
            deleteMovieFromUserListUseCase(movieId, listType, username).also { isDeleted ->
                withContext(mainDispatcher) { handleResult(isDeleted) }
            }
            isLoading.postValue(false)
        }
    }
}