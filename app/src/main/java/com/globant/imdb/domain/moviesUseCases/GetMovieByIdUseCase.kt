package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke(movieId:Int): MovieItem? = repository.getMovieById(movieId)
}