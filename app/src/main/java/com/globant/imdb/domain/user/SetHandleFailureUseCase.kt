package com.globant.imdb.domain.user

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class SetHandleFailureUseCase {
    private val repository = IMDbRepository()

    operator fun invoke( handleFailure:(title:String, msg:String)->Unit ) =
        repository.setHandleFailure(handleFailure)
}