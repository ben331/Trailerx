package com.globant.imdb.domain.model

import com.globant.imdb.data.database.entities.movie.CategoryEntity
import com.globant.imdb.data.database.entities.movie.CategoryType

data class CategoryItem(
    val id: CategoryType,
)

fun CategoryEntity.toDomain():CategoryItem {
    return CategoryItem(id)
}
