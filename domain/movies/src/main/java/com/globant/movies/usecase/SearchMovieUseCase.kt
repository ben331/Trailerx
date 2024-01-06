package com.globant.movies.usecase

import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MovieRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor( private val repository: MovieRepository) {
    suspend operator fun invoke(query:String):List<MovieItem> = repository.searchMovie(query)
}