package tech.benhack.movies.model

import tech.benhack.common.CategoryType
import tech.benhack.common.SyncState

data class SyncCategoryMovieItem (
    val idMovie: Int,
    val idCategory: CategoryType,
    val syncState: SyncState
)