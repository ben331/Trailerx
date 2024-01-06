package com.globant.movies.usecase


import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.movies.di.IoDispatcher
import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class AddMovieToUserListUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(movie:MovieItem, category:CategoryType):Boolean {
        val isAdded = repository.addMovieToCategory(movie, category)

        return if(isAdded) {
            CoroutineScope(ioDispatcher).launch {
                syncUserLocalDataUseCase()
                try {
                    repository.addMovieToCategoryDatabase(movie.id, category)
                }catch (_:Exception){ }
            }
            true
        } else {
            try {
                repository.addMovieToCategoryDatabase(movie.id, category)
                repository.addMovieToSyncDatabase(movie.id, category, SyncState.PENDING_TO_ADD)
                true
            }catch (_:Exception){
                false
            }
        }
    }
}