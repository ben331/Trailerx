package com.globant.imdb.data.database.typeConverters

import androidx.room.TypeConverter
import com.globant.imdb.data.database.entities.movie.MovieListType

class MovieListTypeConverter {
    @TypeConverter
    fun fromString(value:String): MovieListType {
        return MovieListType.valueOf(value)
    }

    @TypeConverter
    fun toString(listType: MovieListType): String {
        return listType.name
    }
}