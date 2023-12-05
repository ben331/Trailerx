package com.globant.imdb.data.database.typeConverters

import androidx.room.TypeConverter
import com.globant.imdb.domain.model.GenreItem

class StringGenreItemConverter {
    @TypeConverter
    fun fromString(value:String): List<GenreItem> {
        val lines = value.split("*")
        val genres = ArrayList<GenreItem>()
        for(line in lines){
            val props = line.split("/")
            genres.add(GenreItem(
                Integer.parseInt(props[0]),
                props[1]
            ))
        }
        return genres
    }

    @TypeConverter
    fun toString(genres: List<GenreItem>): String {
        var value = ""
        for(genre in genres){
            value += "${genre.id}/${genre.name}*"
        }
        return value
    }
}