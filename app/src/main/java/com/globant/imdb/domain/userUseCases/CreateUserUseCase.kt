package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.model.user.UserModel
import javax.inject.Inject

class CreateUserUseCase @Inject constructor( private val repository: IMDbRepository) {
    suspend operator fun invoke(localUser:UserModel):UserModel? = repository.getUser(localUser.email)
}