package tech.benhack.movies.usecase

import tech.benhack.common.SyncState
import tech.benhack.movies.model.toSimple
import tech.benhack.movies.repository.MoviesRepository
import javax.inject.Inject

class SyncUserLocalDataUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val getMovieByIdUseCase: GetMovieByIdUseCase
) {
    suspend operator fun invoke(email:String) {
        val moviesToAdd = repository.getMoviesToSync(SyncState.PENDING_TO_ADD)
        val moviesToDelete = repository.getMoviesToSync(SyncState.PENDING_TO_DELETE)

        if (moviesToAdd != null) {
            for (syncMovie in moviesToAdd) {
                val movie = getMovieByIdUseCase(syncMovie.idMovie)
                movie?.let {
                    val isAdded = repository.addMovieToCategory(it.toSimple(), syncMovie.idCategory, email)
                    if(isAdded) {
                        repository.deleteMovieFromSyncLocal(syncMovie.idMovie, syncMovie.idCategory)
                    }
                }
            }
        }

        if (moviesToDelete != null) {
            for (syncMovie in moviesToDelete) {
                val isDeleted = repository.deleteMovieFromCategory(syncMovie.idMovie, syncMovie.idCategory, email)
                if(isDeleted) {
                    repository.deleteMovieFromSyncLocal(syncMovie.idMovie, syncMovie.idCategory)
                }
            }
        }
    }
}