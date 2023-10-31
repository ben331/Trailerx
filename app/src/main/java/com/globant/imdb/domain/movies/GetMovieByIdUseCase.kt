package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.MovieDetail

class GetMovieByIdUseCase {
    private val repository = IMDbRepository()

    suspend operator fun invoke(movieId:Int): MovieDetail? = repository.getMovieById(movieId)
}