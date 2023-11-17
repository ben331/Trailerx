package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.IMDbRepository
import javax.inject.Inject

class DeleteMovieFromUserListUseCase @Inject constructor(private val repository:IMDbRepository ) {
    operator fun invoke(
        movieId:Int,
        listNumber:Int,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.deleteMovieFromList(movieId, listNumber, handleSuccess, handleFailure)
    }
}