package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "category_movie",
    primaryKeys = ["idMovie", "idCategory"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["idMovie"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryMovieEntity(
    @ColumnInfo(index = true)       val idMovie: Int,
    @ColumnInfo(index = true)       val idCategory: String,
)

fun MovieEntity.toCategoryMovie(category:CategoryType):CategoryMovieEntity {
    return CategoryMovieEntity(id, category.name)
}
