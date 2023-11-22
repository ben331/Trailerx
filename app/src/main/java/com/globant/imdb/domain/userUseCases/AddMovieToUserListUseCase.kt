package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class AddMovieToUserListUseCase @Inject constructor(private val repository:IMDbRepository ) {
    operator fun invoke(
        movie:MovieItem,
        listType:MovieListType,
        handleSuccess:(movie:MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.addMovieToList(movie, listType, handleSuccess, handleFailure)
    }
}