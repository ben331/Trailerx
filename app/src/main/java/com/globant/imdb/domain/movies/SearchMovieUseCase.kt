package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.MovieModel
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke(query:String):List<MovieModel> = repository.searchMovie(query)
}