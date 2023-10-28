package com.globant.imdb.domain

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.Movie
import com.globant.imdb.data.model.MovieDetail

class GetMovieByIdUseCase {
    private val repository = IMDbRepository()

    suspend operator fun invoke(movieId:Int):MovieDetail? = repository.getMovieById(movieId)
}