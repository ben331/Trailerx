package tech.benhack.movies.usecase


import tech.benhack.common.CategoryType
import tech.benhack.common.SyncState
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class AddMovieToUserListUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
) {
    suspend operator fun invoke(movie:MovieItem, category:CategoryType, email:String):Boolean {
        val isAdded = repository.addMovieToCategory(movie, category, email)

        return if(isAdded) {
            CoroutineScope(Dispatchers.IO).launch {
                syncUserLocalDataUseCase(email)
                repository.addMovieToCategoryLocal(movie.id, category)
            }
            true
        } else {
            repository.addMovieToCategoryLocal(movie.id, category).also { addedToLocal ->
                if(addedToLocal)
                    repository.addMovieToSyncLocal(movie.id, category, SyncState.PENDING_TO_ADD)
            }
        }
    }
}