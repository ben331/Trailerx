package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class SearchMovieUseCase {
    private val repository = IMDbRepository()

    suspend operator fun invoke(query:String):List<Movie> = repository.searchMovie(query)
}