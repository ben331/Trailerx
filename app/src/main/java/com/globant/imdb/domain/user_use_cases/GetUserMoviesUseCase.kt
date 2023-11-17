package com.globant.imdb.domain.user_use_cases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.MovieModel
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        numberList:Int,
        handleSuccess:(movies:List<MovieModel>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) =
        repository.getUserMoviesList(numberList, handleSuccess, handleFailure)
}