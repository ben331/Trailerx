package com.globant.movies.usecase

import com.globant.common.SyncState
import com.globant.movies.model.toSimple
import com.globant.movies.repository.MovieRepository
import javax.inject.Inject

class SyncUserLocalDataUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val getMovieByIdUseCase: GetMovieByIdUseCase
) {
    suspend operator fun invoke() {
        val moviesToAdd = repository.getMoviesToSync(SyncState.PENDING_TO_ADD)
        val moviesToDelete = repository.getMoviesToSync(SyncState.PENDING_TO_DELETE)

        for (syncMovie in moviesToAdd) {
            val movie = getMovieByIdUseCase(syncMovie.idMovie)
            movie?.let {
                val isAdded = repository.addMovieToCategory(it.toSimple(), syncMovie.idCategory)
                if(isAdded) {
                    repository.deleteMovieFromSyncDatabase(syncMovie.idMovie, syncMovie.idCategory)
                }
            }
        }

        for (syncMovie in moviesToDelete) {
            val isDeleted = repository.deleteMovieFromCategory(syncMovie.idMovie, syncMovie.idCategory)
            if(isDeleted) {
                repository.deleteMovieFromSyncDatabase(syncMovie.idMovie, syncMovie.idCategory)
            }
        }
    }
}