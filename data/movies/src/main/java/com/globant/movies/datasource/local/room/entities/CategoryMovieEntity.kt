package com.globant.movies.datasource.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import com.globant.movies.datasource.local.room.typeconverter.StringCategoryTypeConverter

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
@TypeConverters(StringCategoryTypeConverter::class)
data class CategoryMovieEntity(
    @ColumnInfo(index = true)       val idMovie: Int,
    @ColumnInfo(index = true)       val idCategory: CategoryType,
)

fun MovieEntity.toCategoryMovie(category: CategoryType): CategoryMovieEntity {
    return CategoryMovieEntity(id, category)
}
