package com.globant.imdb.data.database.typeConverters

import androidx.room.TypeConverter
import com.globant.imdb.data.database.entities.movie.CategoryType

class StringMovieListTypeConverter {
    @TypeConverter
    fun fromString(value:String): CategoryType {
        return CategoryType.valueOf(value)
    }

    @TypeConverter
    fun toString(listType: CategoryType): String {
        return listType.name
    }
}