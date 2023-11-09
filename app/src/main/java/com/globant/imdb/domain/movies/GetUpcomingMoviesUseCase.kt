package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie
import javax.inject.Inject

class GetUpcomingMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke():List<Movie> = repository.getUpcomingMovies()
}