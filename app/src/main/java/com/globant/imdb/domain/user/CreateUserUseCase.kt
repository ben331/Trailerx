package com.globant.imdb.domain.user

import android.content.Context
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.user.User
import javax.inject.Inject

class CreateUserUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        context:Context,
        localUser:User,
        handleSuccess:(user:User?)->Unit
    ){
        repository.getUser(context, localUser.email) { user ->
            if (user == null) {
                repository.createUser(localUser, handleSuccess)
            } else {
                handleSuccess(user)
            }
        }
    }
}