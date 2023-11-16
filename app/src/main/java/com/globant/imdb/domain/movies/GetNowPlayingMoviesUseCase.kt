package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.MovieModel
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke():List<MovieModel> = repository.getNowPlayingMovies()
}