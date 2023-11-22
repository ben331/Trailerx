package com.globant.imdb.domain.userUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    operator fun invoke(
        listType: MovieListType,
        handleSuccess:(movies:List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ) =
        repository.getUserMoviesList(listType, handleSuccess, handleFailure)
}