package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class AddMovieToUserListUseCase @Inject constructor(private val repository: IMDbRepository) {
    operator fun invoke(
        movie:MovieItem,
        listType:CategoryType,
        handleSuccess:(movie:MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) {
        return repository.addMovieToList(movie, listType, handleSuccess, handleFailure)
    }
}