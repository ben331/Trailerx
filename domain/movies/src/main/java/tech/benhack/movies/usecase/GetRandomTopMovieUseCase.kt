package tech.benhack.movies.usecase

import tech.benhack.common.CategoryType
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.repository.MoviesRepository
import javax.inject.Inject

class GetRandomTopMovieUseCase @Inject constructor( private val repository: MoviesRepository){
    suspend operator fun invoke(): MovieItem? {
        val movies = repository
            .getMoviesByCategoryFromLocal(CategoryType.NOW_PLAYING_MOVIES)
            .toRightValueOrNull()

        if(movies!=null){
            val indices = if (movies.size > 10 )  (0..9) else movies.indices
            return movies[indices.random()]
        }
        return null
    }
}