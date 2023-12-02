package com.globant.imdb.domain.moviesUseCases

import android.util.Log
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository: IMDbRepository) {
    suspend operator fun invoke(movieId:Int):MovieItem? {
        val movie = repository.getMovieByIdFromDatabase(movieId)
        return if (movie != null ) {
            if(movie.tagline.isNullOrEmpty()){
                repository.getMovieByIdFromApi(movieId)?.let { movieItem ->
                    repository.updateMovie(movieItem.toDatabase())
                    movieItem
                }
                movie
            }else{
                movie
            }
        } else {
            repository.getMovieByIdFromApi(movieId)
        }
    }
}