package tech.benhack.movies.datasource.local.room.typeconverter

import androidx.room.TypeConverter
import tech.benhack.common.CategoryType

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