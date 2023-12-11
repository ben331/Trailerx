package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.toPendingToDelete
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class DeleteMovieFromUserListUseCase @Inject constructor(
    private val repository: IMDbRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase
) {
    operator fun invoke(
        movieId:Int,
        category: CategoryType,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        fun handleRemoteSuccess(){
            CoroutineScope(Dispatchers.IO).launch {
                syncUserLocalDataUseCase()
                try {
                    repository.deleteMovieFromCategoryDatabase(movieId, category)
                }catch (e: Exception){
                    e.printStackTrace()
                }finally {
                    handleSuccess()
                }
            }
        }

        fun handleRemoteFailure(title:Int, msg:Int){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    repository.deleteMovieFromCategoryDatabase(movieId, category)
                    repository.addMovieToCategoryDatabase(movieId, category.toPendingToDelete())
                }catch (e: Exception){
                    handleFailure(title, msg)
                }
            }
        }

        repository.deleteMovieFromCategory(movieId, category, ::handleRemoteSuccess, ::handleRemoteFailure)
    }
}