package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetRandomTopMovieUseCase @Inject constructor( private val repository:IMDbRepository ){
    suspend operator fun invoke(): MovieItem? {
        val movies = repository.getMoviesByCategoryFromDatabase(CategoryType.NOW_PLAYING_MOVIES)

        if(movies.isNotEmpty()){
            val indices = if (movies.size > 10 )  (0..9) else movies.indices
            return movies[indices.random()]
        }
        return null
    }
}