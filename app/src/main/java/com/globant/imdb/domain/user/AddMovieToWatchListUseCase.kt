package com.globant.imdb.domain.user

import android.content.Context
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class AddMovieToWatchListUseCase {
    private val repository = IMDbRepository()

    operator fun invoke(context:Context, movie:Movie) = repository.addMovieToWatchList(context, movie)
}