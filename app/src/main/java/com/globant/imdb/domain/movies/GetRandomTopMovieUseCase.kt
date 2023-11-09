package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie
import javax.inject.Inject

class GetRandomTopMovieUseCase @Inject constructor( private val repository:IMDbRepository ){
    suspend operator fun invoke(): Movie? {
        val movies = repository.getNowPlayingMovies()
        if(movies.isNotEmpty()){
            val indices = if (movies.size > 10 )  (0..9) else movies.indices
            return movies[indices.random()]
        }
        return null
    }
}