package com.globant.movies.usecase

import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.movies.di.IoDispatcher
import com.globant.movies.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteMovieFromUserListUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(movieId:Int, category: CategoryType):Boolean {
        val isDeleted = repository.deleteMovieFromCategory(movieId, category)

        return if(isDeleted){
            CoroutineScope(ioDispatcher).launch {
                syncUserLocalDataUseCase()
                try {
                    repository.deleteMovieFromCategoryDatabase(movieId, category)
                } catch (_:Exception){ }
            }
            true
        } else {
            try {
                repository.deleteMovieFromCategoryDatabase(movieId, category)
                repository.addMovieToSyncDatabase(movieId, category, SyncState.PENDING_TO_DELETE)
                true
            }catch (_: Exception){ false }
        }
    }
}