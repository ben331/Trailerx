package com.globant.imdb.domain.user

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class SetupUserRepositoryUseCase {
    private val repository = IMDbRepository()

    operator fun invoke(
        handleSuccessGetMovies:(movies:ArrayList<Movie>)->Unit,
        handleSuccessAddMovie:(movie:Movie)->Unit,
        handleFailure:(title:String, msg:String)->Unit
    ) {
        repository.setupUserRepository(
            handleSuccessGetMovies,
            handleSuccessAddMovie,
            handleFailure
        )
    }
}