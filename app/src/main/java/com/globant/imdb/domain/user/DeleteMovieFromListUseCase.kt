package com.globant.imdb.domain.user

import android.content.Context
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class DeleteMovieFromListUseCase {
    private val repository = IMDbRepository()

    operator fun invoke(
        context: Context,
        movieId:Int,
        listNumber:Int,
        handleSuccess:()->Unit
    ) =
        repository.deleteMovieFromList(context, movieId, listNumber, handleSuccess)
}