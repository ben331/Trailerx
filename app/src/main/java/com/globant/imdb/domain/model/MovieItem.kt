package com.globant.imdb.domain.model

import com.globant.imdb.data.database.entities.movie.MovieEntity
import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.imdb.data.model.movies.MovieModel

data class MovieItem(
    val adult: Boolean? = false,
    val backdropPath: String? = "",
    val genreNames: List<String>? = emptyList(),
    val id: Int = 0,
    val originalLanguage: String? = "",
    val originalTitle: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val posterPath: String? = "",
    val releaseDate: String? = "",
    val title: String? = "",
    val video: Boolean? = false,
    val voteAverage: Double? = 0.0,
    val voteCount: Int? = 0,
    val tagline:String? = "",
)

fun MovieEntity.toDomain():MovieItem {
    return MovieItem(
        adult = adult,
        backdropPath = backdropPath,
        genreNames = genreNames,
        id = id,
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
        tagline = tagline,
    )
}

fun MovieModel.toDomain():MovieItem {
    return MovieItem(
        adult = adult,
        backdropPath = backdropPath,
        genreNames = emptyList(),
        id = id,
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
    )
}

fun MovieDetailModel.toDomain():MovieItem {

    val genreNames = genres.map { it.name }

    return MovieItem(
        adult = adult,
        backdropPath = backdropPath,
        genreNames = genreNames,
        id = id,
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
        tagline = tagline,
    )
}
