package com.globant.imdb.domain.user

import android.content.Context
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie
import javax.inject.Inject

class GetUserMoviesUseCase @Inject constructor(private val repository:IMDbRepository ) {
    operator fun invoke(context:Context, numberList:Int, handleSuccess:(movies:List<Movie>)->Unit) =
        repository.getUserMoviesList(context, numberList, handleSuccess)
}