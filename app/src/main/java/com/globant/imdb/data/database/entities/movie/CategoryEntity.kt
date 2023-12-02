package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.globant.imdb.data.database.typeConverters.StringMovieListTypeConverter

enum class CategoryType {
    NOW_PLAYING_MOVIES,
    POPULAR_MOVIES,
    UPCOMING_MOVIES,
    WATCH_LIST_MOVIES,
    HISTORY_MOVIES,
    /*TODO DELETE FAVORITE PEOPLE, IT DOES NOT BELONG TO MovieListType*/
    FAVORITE_PEOPLE
}

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    @TypeConverters(StringMovieListTypeConverter::class)
    @ColumnInfo val id:CategoryType
)

fun CategoryType.toDatabase(): CategoryEntity{
    return CategoryEntity(this)
}