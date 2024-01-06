package com.globant.movies.usecase

import com.globant.common.CategoryType
import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MovieRepository
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor( private val repository: MovieRepository){
    suspend operator fun invoke():List<MovieItem> {
        val movies = repository.getPopularMoviesFromApi()

        val category = CategoryType.POPULAR_MOVIES
        return if ( movies.isNotEmpty() ) {
            repository.clearMoviesByCategoryDatabase(category)
            repository.addMoviesToCategoryDatabase( movies, category )
            movies
        } else {
            repository.getMoviesByCategoryFromDatabase(category).toRightValueOrNull() ?: emptyList()
        }
    }
}