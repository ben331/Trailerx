package com.globant.movies.usecase

import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.movies.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteMovieFromUserListUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
) {
    suspend operator fun invoke(movieId:Int, category: CategoryType, email:String):Boolean {
        val isDeleted = repository.deleteMovieFromCategory(movieId, category, email)

        return if(isDeleted){
            CoroutineScope(Dispatchers.IO).launch {
                syncUserLocalDataUseCase(email)
                repository.deleteMovieFromCategoryLocal(movieId, category)
            }
            true
        } else {
            repository.deleteMovieFromCategoryLocal(movieId, category).also { deletedFromLocal ->
                if(deletedFromLocal)
                    repository.addMovieToSyncLocal(movieId, category, SyncState.PENDING_TO_DELETE)
            }
        }
    }
}