package com.globant.imdb.domain.model

import com.globant.imdb.data.database.entities.movie.MovieListEntity
import com.globant.imdb.data.database.entities.movie.MovieListType

data class MovieListItem(
    val id: MovieListType,
)

fun MovieListEntity.toDomain():MovieListItem {
    return MovieListItem(id)
}
