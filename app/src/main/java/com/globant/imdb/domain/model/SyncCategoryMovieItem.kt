package com.globant.imdb.domain.model

import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.SyncCategoryMovieEntity
import com.globant.imdb.data.database.entities.movie.SyncState

data class SyncCategoryMovieItem (
    val idMovie: Int,
    val idCategory: CategoryType,
    val syncState: SyncState
)

fun SyncCategoryMovieEntity.toDomain(): SyncCategoryMovieItem {
    return SyncCategoryMovieItem(idMovie, idCategory, syncState)
}