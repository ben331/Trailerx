package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.database.entities.movie.MovieListType
import javax.inject.Inject

class DeleteMovieFromUserListUseCase @Inject constructor(private val repository:IMDbRepository ) {
    operator fun invoke(
        movieId:Int,
        listType: MovieListType,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.deleteMovieFromList(movieId, listType, handleSuccess, handleFailure)
    }
}