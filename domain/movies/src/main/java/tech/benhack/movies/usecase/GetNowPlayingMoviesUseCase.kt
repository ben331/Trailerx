package tech.benhack.movies.usecase

import tech.benhack.common.CategoryType
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.repository.MoviesRepository
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor( private val repository: MoviesRepository) {
    suspend operator fun invoke():List<MovieItem> {
        val movies = repository.getNowPlayingMoviesFromApi()

        val category = CategoryType.NOW_PLAYING_MOVIES
        return if ( movies.isNotEmpty() ) {
            repository.clearMoviesByCategoryLocal(category)
            repository.addMoviesToCategoryLocal( movies, category )
            movies
        } else {
            repository.getMoviesByCategoryFromLocal(category).toRightValueOrNull() ?: emptyList()
        }
    }
}