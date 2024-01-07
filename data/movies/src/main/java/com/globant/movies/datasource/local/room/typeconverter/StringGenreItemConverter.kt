package com.globant.movies.datasource.local.room.typeconverter

import androidx.room.TypeConverter
import com.globant.movies.model.GenreItem
import java.lang.NumberFormatException

class StringGenreItemConverter {
    @TypeConverter
    fun fromString(value:String?): List<GenreItem>? {
        return if(!value.isNullOrBlank()){
            val lines = value.split("*")
            val genres = ArrayList<GenreItem>()
            for(line in lines){
                val props = line.split("%")
                try {
                    genres.add(GenreItem(
                        Integer.parseInt(props[0]),
                        props[1]
                    ))
                }catch (e:NumberFormatException){
                    e.printStackTrace()
                }
            }
            genres
        } else {
            null
        }

    }

    @TypeConverter
    fun toString(genres: List<GenreItem>?): String? {
        return if (!genres.isNullOrEmpty()){
            var value = ""
            for(genre in genres){
                value += "${genre.id}%${genre.name}*"
            }
            value
        } else {
            null
        }

    }
}