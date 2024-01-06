package com.globant.movies.usecase

import com.globant.movies.model.MovieDetailItem
import com.globant.movies.repository.MovieRepository
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository: MovieRepository) {
    suspend operator fun invoke(movieId:Int):MovieDetailItem? {
        val movie = repository.getMovieByIdFromDatabase(movieId)
        if(movie == null || movie.tagline.isNullOrEmpty()) {
            val movieUpdated = repository.getMovieByIdFromApi(movieId)
            if(movieUpdated != null){
                repository.addMovieDetailDatabase(movieUpdated)
                return movieUpdated
            }
        }
        return movie
    }
}