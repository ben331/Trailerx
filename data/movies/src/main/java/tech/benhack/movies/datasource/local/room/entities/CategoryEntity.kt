package tech.benhack.movies.datasource.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import tech.benhack.common.CategoryType
import tech.benhack.movies.datasource.local.room.typeconverter.StringCategoryTypeConverter

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    @TypeConverters(StringCategoryTypeConverter::class)
    @ColumnInfo val id: CategoryType
)