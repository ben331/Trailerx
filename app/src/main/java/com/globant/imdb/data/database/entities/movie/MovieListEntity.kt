package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_lists")
data class MovieListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id: Int,
    @ColumnInfo val listName:Int
)