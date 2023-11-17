package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        numberList:Int,
        handleSuccess:(movies:List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) =
        repository.getUserMoviesList(numberList, handleSuccess, handleFailure)
}