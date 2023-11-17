package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.globant.imdb.data.database.typeConverters.MovieListTypeConverter

enum class MovieListType {
    NOW_PLAYING_MOVIES,
    POPULAR_MOVIES,
    UPCOMING_MOVIES,
    WATCH_LIST_MOVIES,
    HISTORY_MOVIES
}

@Entity(tableName = "movies_lists")
data class MovieListEntity(
    @PrimaryKey
    @TypeConverters(MovieListTypeConverter::class)
    @ColumnInfo val id:MovieListType
)

fun MovieListType.toDatabase(): MovieListEntity{
    return MovieListEntity(this)
}