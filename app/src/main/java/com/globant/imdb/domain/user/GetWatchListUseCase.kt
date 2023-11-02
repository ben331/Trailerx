package com.globant.imdb.domain.user

import android.content.Context
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Movie

class GetWatchListUseCase {
    private val repository = IMDbRepository()

    operator fun invoke(context:Context, handleSuccess:(movies:ArrayList<Movie>)->Unit) =
        repository.getWatchList(context, handleSuccess)
}