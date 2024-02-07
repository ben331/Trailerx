package tech.benhack.movies.usecase

import tech.benhack.common.CategoryType
import tech.benhack.common.Either
import tech.benhack.common.ErrorData
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository,
    private val syncUserLocalDataUseCase: SyncUserLocalDataUseCase,
) {
    suspend operator fun invoke(category: CategoryType, email:String): Either<ErrorData,List<MovieItem>> {
        return when(val result = repository.getUserMoviesList(category, email)){
            is Either.Right -> {
                CoroutineScope(Dispatchers.IO).launch {
                    syncUserLocalDataUseCase(email)
                    repository.clearMoviesByCategoryLocal(category)
                    repository.addMoviesToCategoryLocal(result.r, category)
                }
                result
            }
            is Either.Left -> {
                repository.getMoviesByCategoryFromLocal(category)
            }
        }
    }
}