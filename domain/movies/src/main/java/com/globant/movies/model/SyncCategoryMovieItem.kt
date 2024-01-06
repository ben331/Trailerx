package com.globant.movies.model

import com.globant.common.CategoryType
import com.globant.common.SyncState

data class SyncCategoryMovieItem (
    val idMovie: Int,
    val idCategory: CategoryType,
    val syncState: SyncState
)