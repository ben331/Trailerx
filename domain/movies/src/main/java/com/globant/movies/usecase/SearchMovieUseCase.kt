package com.globant.movies.usecase

import com.globant.movies.model.MovieItem
import com.globant.movies.repository.MoviesRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor( private val repository: MoviesRepository) {
    suspend operator fun invoke(query:String):List<MovieItem> = repository.searchMovie(query)
}