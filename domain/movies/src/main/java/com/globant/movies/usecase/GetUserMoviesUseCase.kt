package com.globant.movies.usecase

import com.globant.common.CategoryType
import com.globant.common.Either
import com.globant.common.ErrorData
import com.globant.movies.di.IoDispatcher
import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
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
                        repository.addMoviesToCategoryDatabase(result.r, category)
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