package com.globant.movies.datasource.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.globant.movies.datasource.local.room.typeconverter.StringGenreItemConverter
import com.globant.imdb.domain.model.GenreItem
import com.globant.movies.model.MovieDetailItem
@TypeConverters(StringGenreItemConverter::class)
@Entity(tableName = "movie_detail")
data class MovieDetailEntity(
    @ColumnInfo("adult")                    val adult: Boolean?,
    @ColumnInfo("backdrop_path")            val backdropPath: String?,
    @ColumnInfo("budget")                   val budget: Int?,
    @ColumnInfo("genres")                   val genres: List<GenreItem>? = emptyList(),
    @ColumnInfo("homepage")                 val homepage:String? = "",
    @PrimaryKey
    @ColumnInfo("id")                       val id: Int = 0,
    @ColumnInfo("imdb_id")                  val imdbId:String? = "",
    @ColumnInfo("original_language")        val originalLanguage: String? = "",
    @ColumnInfo("original_title")           val originalTitle: String? = "",
    @ColumnInfo("overview")                 val overview: String? = "",
    @ColumnInfo("popularity")               val popularity: Double? = 0.0,
    @ColumnInfo("poster_path")              val posterPath: String? = "",
    @ColumnInfo("release_date")             val releaseDate: String? = "",
    @ColumnInfo("revenue")                  val revenue: Int? = 0,
    @ColumnInfo("runtime")                  val runtime:Int? = 0,
    @ColumnInfo("status")                   val status: String? = "",
    @ColumnInfo("tagline")                  val tagline:String? = "",
    @ColumnInfo("title")                    val title: String? = "",
    @ColumnInfo("video")                    val video: Boolean? = false,
    @ColumnInfo("vote_average")             val voteAverage: Double? = 0.0,
    @ColumnInfo("vote_count")               val voteCount: Int? = 0
)

fun MovieDetailItem.toDatabase(): MovieDetailEntity =
    MovieDetailEntity(
        adult = adult,
        backdropPath = backdropPath,
        budget = budget,
        genres = genres,
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        revenue = revenue,
        runtime = runtime,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )