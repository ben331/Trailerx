package com.globant.imdb.domain.user_use_cases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.MovieModel
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class AddMovieToListUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        movie:MovieItem,
        listNumber: Int,
        handleSuccess:(movie:MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.addMovieToList(movie, listNumber, handleSuccess, handleFailure)
    }
}