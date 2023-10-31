package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class GetNowPlayingMoviesUseCase {
    private val repository = IMDbRepository()

    suspend operator fun invoke():List<Movie> = repository.getNowPlayingMovies()
}