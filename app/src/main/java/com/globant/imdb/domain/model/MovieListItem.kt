package com.globant.imdb.domain.model

import com.globant.imdb.data.database.entities.movie.MovieListEntity

data class MovieListItem(
    val id: Int,
    val listName:Int
)

fun MovieListEntity.toDomain():MovieListItem {
    return MovieListItem(id, listName)
}
