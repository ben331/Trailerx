package com.globant.imdb.domain.user

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.user.UserModel
import javax.inject.Inject

class CreateUserUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        localUser:UserModel,
        handleSuccess:(user:UserModel?)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        repository.getUser(
            localUser.email,
            { user ->
                if (user == null) {
                    repository.createUser(localUser, handleSuccess)
                } else {
                    handleSuccess(user)
                }
            },
            handleFailure
        )
    }
}