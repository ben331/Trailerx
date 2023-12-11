package com.globant.imdb.domain.moviesUseCases


import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.domain.model.MovieDetailItem
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository: IMDbRepository) {
    suspend operator fun invoke(movieId:Int):MovieDetailItem? {
        val movie = repository.getMovieByIdFromDatabase(movieId)
        if(movie == null || movie.tagline.isNullOrEmpty()) {
            val movieUpdated = repository.getMovieByIdFromApi(movieId)
            if(movieUpdated != null){
                repository.addMovieDetailDatabase(movieUpdated.toDatabase())
                return movieUpdated
            }
        }
        return movie
    }
}