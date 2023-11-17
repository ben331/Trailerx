package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke(query:String):List<MovieItem> = repository.searchMovie(query)
}