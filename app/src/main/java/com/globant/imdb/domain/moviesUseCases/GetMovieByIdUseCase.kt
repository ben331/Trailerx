package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke(movieId:Int):MovieItem? {
        val movie = repository.getMovieByIdFromDatabase(movieId)

        return if (movie != null ) {
            if(movie.tagline == ""){
                repository.getMovieByIdFromApi(movieId)?.let {
                    repository.updateMovieTagLine(it.id, it.tagline)
                }
            }
            movie
        } else {
            repository.getMovieByIdFromApi(movieId)
        }
    }
}