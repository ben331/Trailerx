package com.globant.imdb.domain.user_use_cases

import com.globant.imdb.data.IMDbRepository
import javax.inject.Inject

class DeleteMovieFromListUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        movieId:Int,
        listNumber:Int,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.deleteMovieFromList(movieId, listNumber, handleSuccess, handleFailure)
    }
}