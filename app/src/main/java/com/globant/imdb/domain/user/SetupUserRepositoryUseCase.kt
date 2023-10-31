package com.globant.imdb.domain.user

import android.content.Context
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class SetupUserRepositoryUseCase {
    private val repository = IMDbRepository()

    operator fun invoke(
        context: Context,
        handleSuccessGetMovies:(movies:ArrayList<Movie>)->Unit,
        handleSuccessAddMovie:(movie:Movie)->Unit,
        handleFailure:(title:String, msg:String)->Unit
    ) {
        repository.setupUserRepository(
            context,
            handleSuccessGetMovies,
            handleSuccessAddMovie,
            handleFailure
        )
    }
}