package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.globant.imdb.data.database.typeConverters.StringListConverter
import com.globant.imdb.domain.model.MovieItem
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo("id")                   val id: Int = 0,
    @ColumnInfo("adult")                val adult: Boolean? = false,
    @ColumnInfo("backdrop_path")        val backdropPath: String? = "",
    @ColumnInfo("genre_ids")            val genreIds: List<Int> = emptyList(),
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
    @ColumnInfo("tagline")              val tagline:String? = "",
)

fun MovieItem.toDatabase(): MovieEntity =
    MovieEntity(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        tagline = tagline
    )