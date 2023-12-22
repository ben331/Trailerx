package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.SyncState
import com.globant.imdb.di.IoDispatcher
import com.globant.imdb.domain.model.MovieItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class AddMovieToUserListUseCase @Inject constructor(
    private val repository: IMDbRepository,
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