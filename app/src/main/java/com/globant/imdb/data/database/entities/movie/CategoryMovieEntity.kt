package com.globant.imdb.data.database.entities.movie

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "category_movie",
    primaryKeys = ["idCategory", "idMovie"],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["idMovie"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryMovieEntity(
    val idMovie: Int,
    val idCategory: String,
)

fun MovieEntity.toCategoryMovie(category:CategoryType):CategoryMovieEntity {
    return CategoryMovieEntity(id, category.name)
}
