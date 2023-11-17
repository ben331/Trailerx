package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    foreignKeys = [ ForeignKey(
        entity = MovieListEntity::class,
        parentColumns = ["id"],
        childColumns = ["listId"]
    )]
)
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo("id")                   val id: Int = 0,
    @ColumnInfo("listId")               val listId:Int = 0,
    @ColumnInfo("adult")                val adult: Boolean = false,
    @ColumnInfo("backdrop_path")        val backdropPath: String = "",
    @ColumnInfo("genre_names")          val genreNames: List<String> = emptyList(),
    @ColumnInfo("original_language")    val originalLanguage: String = "",
    @ColumnInfo("original_title")       val originalTitle: String = "",
    @ColumnInfo("overview")             val overview: String = "",
    @ColumnInfo("popularity")           val popularity: Double = 0.0,
    @ColumnInfo("poster_path")          val posterPath: String = "",
    @ColumnInfo("release_date")         val releaseDate: String = "",
    @ColumnInfo("title")                val title: String = "",
    @ColumnInfo("video")                val video: Boolean = false,
    @ColumnInfo("vote_average")         val voteAverage: Double = 0.0,
    @ColumnInfo("vote_count")           val voteCount: Int = 0,
    @ColumnInfo("tagline")              val tagline:String,
)