package com.globant.imdb.domain.userUseCases

import com.globant.common.Either
import com.globant.common.ErrorData
import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.di.IoDispatcher
import com.globant.imdb.domain.model.MovieItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor(
    private val repository: IMDbRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(category: CategoryType): Either<ErrorData,List<MovieItem>> {
        return when(val result = repository.getUserMoviesList(category)){
            is Either.Right -> {
                CoroutineScope(ioDispatcher).launch {
                    syncUserLocalDataUseCase()
                    try {
                        repository.clearMoviesByCategoryDatabase(category)
                        repository.addMoviesToCategoryDatabase(result.r.map { it.toDatabase() }, category)
                    }catch (_: Exception){ }
                }
                result
            }
            is Either.Left -> {
                repository.getMoviesByCategoryFromDatabase(category)
            }
        }
    }
}