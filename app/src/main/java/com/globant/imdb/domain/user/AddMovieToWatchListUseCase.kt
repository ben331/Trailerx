package com.globant.imdb.domain.user

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class AddMovieToWatchListUseCase {
    private val repository = IMDbRepository()

    operator fun invoke(movie:Movie) = repository.addMovieToWatchList(movie)
}