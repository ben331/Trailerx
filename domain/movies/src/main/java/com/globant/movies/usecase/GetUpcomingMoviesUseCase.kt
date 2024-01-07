package com.globant.movies.usecase

import com.globant.common.CategoryType
import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MoviesRepository
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor( private val repository: MoviesRepository) {
    suspend operator fun invoke():List<MovieItem> {
        val movies = repository.getUpcomingMoviesFromApi()

        val category = CategoryType.UPCOMING_MOVIES
        return if ( movies.isNotEmpty() ) {
            repository.clearMoviesByCategoryLocal(category)
            repository.addMoviesToCategoryLocal( movies, category )
            movies
        } else {
            repository.getMoviesByCategoryFromLocal(category).toRightValueOrNull() ?: emptyList()
        }
    }
}