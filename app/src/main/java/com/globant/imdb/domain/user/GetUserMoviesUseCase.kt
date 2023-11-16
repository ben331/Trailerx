package com.globant.imdb.domain.user

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        numberList:Int,
        handleSuccess:(movies:List<Movie>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) =
        repository.getUserMoviesList(numberList, handleSuccess, handleFailure)
}