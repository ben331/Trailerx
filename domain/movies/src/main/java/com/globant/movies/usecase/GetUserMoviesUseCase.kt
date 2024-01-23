package com.globant.movies.usecase

import com.globant.common.CategoryType
import com.globant.common.Either
import com.globant.common.ErrorData
import com.globant.common.di.IoDispatcher
import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(category: CategoryType, email:String): Either<ErrorData,List<MovieItem>> {
        return when(val result = repository.getUserMoviesList(category, email)){
            is Either.Right -> {
                CoroutineScope(ioDispatcher).launch {
                    syncUserLocalDataUseCase(email)
                    try {
                        repository.clearMoviesByCategoryLocal(category)
                        repository.addMoviesToCategoryLocal(result.r, category)
                    }catch (_: Exception){ }
                }
                result
            }
            is Either.Left -> {
                repository.getMoviesByCategoryFromLocal(category)
            }
        }
    }
}