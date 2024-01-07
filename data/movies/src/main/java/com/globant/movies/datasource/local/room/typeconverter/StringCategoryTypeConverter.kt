package com.globant.movies.datasource.local.room.typeconverter

import androidx.room.TypeConverter
import com.globant.common.CategoryType

class StringCategoryTypeConverter {
    @TypeConverter
    fun fromString(value:String): CategoryType {
        return CategoryType.valueOf(value)
    }

    @TypeConverter
    fun toString(listType: CategoryType): String {
        return listType.name
    }
}