package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.SyncState
import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.model.toSimple
import com.globant.imdb.domain.moviesUseCases.GetMovieByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncUserLocalDataUseCase @Inject constructor(
    private val repository: IMDbRepository,
    private val getMovieByIdUseCase: GetMovieByIdUseCase
) {
    suspend operator fun invoke() {
        val moviesToAdd = repository.getMoviesToSync(SyncState.PENDING_TO_ADD)
        val moviesToDelete = repository.getMoviesToSync(SyncState.PENDING_TO_DELETE)

        for (syncMovie in moviesToAdd) {
            val movie = getMovieByIdUseCase(syncMovie.idMovie)
            movie?.let {
                repository.addMovieToCategory(it.toSimple(), syncMovie.idCategory, ::handleSuccessSync) { _, _ -> }
            }
        }

        for (syncMovie in moviesToDelete) {
            repository.deleteMovieFromCategory(syncMovie.idMovie, syncMovie.idCategory, ::handleSuccessSync) { _, _ -> }
        }
    }

    private fun handleSuccessSync(movieItem: MovieItem, categoryType: CategoryType){
        handleSuccessSync(movieItem.id, categoryType)
    }

    private fun handleSuccessSync(movieId: Int, categoryType: CategoryType){
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteMovieFromSyncDatabase(movieId, categoryType)
        }
    }
}