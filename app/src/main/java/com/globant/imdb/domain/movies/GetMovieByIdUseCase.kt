package com.globant.imdb.domain.movies

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.MovieDetailModel
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke(movieId:Int): MovieDetailModel? = repository.getMovieById(movieId)
}