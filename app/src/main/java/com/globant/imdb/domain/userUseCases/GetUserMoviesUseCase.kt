package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor( private val repository: IMDbRepository) {
    operator fun invoke(
        listType: CategoryType,
        handleSuccess:(movies:List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) =
        repository.getUserMoviesList(listType, handleSuccess, handleFailure)
}