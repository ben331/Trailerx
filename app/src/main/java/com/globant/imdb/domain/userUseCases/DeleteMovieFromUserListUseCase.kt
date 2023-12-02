package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import javax.inject.Inject

class DeleteMovieFromUserListUseCase @Inject constructor(private val repository: IMDbRepository) {
    operator fun invoke(
        movieId:Int,
        listType: CategoryType,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.deleteMovieFromList(movieId, listType, handleSuccess, handleFailure)
    }
}