package com.globant.movies.usecase

import com.globant.common.CategoryType
import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MoviesRepository
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