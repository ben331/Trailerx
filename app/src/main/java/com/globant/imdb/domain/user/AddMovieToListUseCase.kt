package com.globant.imdb.domain.user

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie
import javax.inject.Inject

class AddMovieToListUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        movie:Movie,
        listNumber: Int,
        handleSuccess:(movie:Movie)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.addMovieToList(movie, listNumber, handleSuccess, handleFailure)
    }
}