package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.di.IoDispatcher
import com.globant.imdb.domain.model.MovieItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor(
    private val repository: IMDbRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(category: CategoryType): List<MovieItem>? {
        val movies = repository.getUserMoviesList(category)
        return if(movies!=null) {
            CoroutineScope(ioDispatcher).launch {
                syncUserLocalDataUseCase()
                try {
                    repository.clearMoviesByCategoryDatabase(category)
                    repository.addMoviesToCategoryDatabase(movies.map { it.toDatabase() }, category)
                }catch (_: Exception){ }
            }
            movies
        }else {
            repository.getMoviesByCategoryFromDatabase(category).ifEmpty { null }
        }
    }
}