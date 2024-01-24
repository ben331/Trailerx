package com.globant.movies.usecase


import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MoviesRepository
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
                try {
                    repository.addMovieToCategoryLocal(movie.id, category)
                }catch (_:Exception){ }
            }
            true
        } else {
            try {
                repository.addMovieToCategoryLocal(movie.id, category)
                repository.addMovieToSyncLocal(movie.id, category, SyncState.PENDING_TO_ADD)
                true
            }catch (_:Exception){
                false
            }
        }
    }
}