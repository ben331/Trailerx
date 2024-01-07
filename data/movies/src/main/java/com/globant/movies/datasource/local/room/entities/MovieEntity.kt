package com.globant.movies.datasource.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globant.movies.model.MovieItem

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo("id")                   val id: Int = 0,
    @ColumnInfo("adult")                val adult: Boolean? = false,
    @ColumnInfo("backdrop_path")        val backdropPath: String? = "",
    @ColumnInfo("original_language")    val originalLanguage: String? = "",
    @ColumnInfo("original_title")       val originalTitle: String? = "",
    @ColumnInfo("overview")             val overview: String? = "",
    @ColumnInfo("popularity")           val popularity: Double? = 0.0,
    @ColumnInfo("poster_path")          val posterPath: String? = "",
    @ColumnInfo("release_date")         val releaseDate: String? = "",
    @ColumnInfo("title")                val title: String? = "",
    @ColumnInfo("video")                val video: Boolean? = false,
    @ColumnInfo("vote_average")         val voteAverage: Double? = 0.0,
    @ColumnInfo("vote_count")           val voteCount: Int? = 0,
)