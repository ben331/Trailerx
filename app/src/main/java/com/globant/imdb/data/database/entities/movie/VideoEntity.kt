package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "videos")
data class VideoEntity(
    @ColumnInfo("iso_639_1")        val iso6391:String,
    @ColumnInfo("iso_3166_1")       val iso31661:String,
    @ColumnInfo("name")             val name:String,
    @ColumnInfo("key")              val key:String,
    @ColumnInfo("site")             val site:String,
    @ColumnInfo("size")             val size:Int,
    @ColumnInfo("type")             val type:String,
    @ColumnInfo("official")         val official:Boolean,
    @ColumnInfo("published_at")     val publishedAt: String,
    @ColumnInfo("id")               val id:String
)
