package com.globant.imdb.domain

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.Movie

class GetNowPlayingMoviesUseCase {
    private val repository = IMDbRepository()

    suspend operator fun invoke():List<Movie> = repository.getNowPlayingMovies()
}