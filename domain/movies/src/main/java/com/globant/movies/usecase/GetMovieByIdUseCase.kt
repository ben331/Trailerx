package com.globant.movies.usecase

import com.globant.movies.model.MovieDetailItem
import com.globant.movies.repository.MoviesRepository
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository: MoviesRepository) {
    suspend operator fun invoke(movieId:Int):MovieDetailItem? {
        val movie = repository.getMovieByIdFromLocal(movieId)
        if(movie == null || movie.tagline.isNullOrEmpty()) {
            val movieUpdated = repository.getMovieByIdFromApi(movieId)
            if(movieUpdated != null){
                repository.addMovieDetailLocal(movieUpdated)
                return movieUpdated
            }
        }
        return movie
    }
}