package com.globant.imdb.domain.user

import com.globant.imdb.data.IMDbRepository
import javax.inject.Inject

class SetHandleFailureUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke( handleFailure:(title:String, msg:String)->Unit ) =
        repository.setHandleFailure(handleFailure)
}