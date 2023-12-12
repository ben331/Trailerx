package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.SyncState
import com.globant.imdb.domain.model.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class AddMovieToUserListUseCase @Inject constructor(
    private val repository: IMDbRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase
) {
    operator fun invoke(
        movie:MovieItem,
        category:CategoryType,
        handleSuccess:(movie:MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        fun handleRemoteSuccess(movie:MovieItem, category:CategoryType){
            CoroutineScope(Dispatchers.IO).launch {
                syncUserLocalDataUseCase()
                try {
                    repository.addMovieToCategoryDatabase(movie.id, category)
                    withContext(Dispatchers.Main){
                        handleSuccess(movie)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

        fun handleRemoteFailure(title:Int, msg:Int){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.addMovieToCategoryDatabase(movie.id, category)
                    repository.addMovieToSyncDatabase(movie.id, category, SyncState.PENDING_TO_ADD)
                    withContext(Dispatchers.Main){
                        handleSuccess(movie)
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        handleFailure(title, msg)
                    }
                }
            }
        }

        repository.addMovieToCategory(movie, category, ::handleRemoteSuccess, ::handleRemoteFailure)
    }
}