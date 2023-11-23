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
        adult,
        backdropPath,
        genreNames,
        id,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        releaseDate,
        tagline,
    )
}

fun MovieModel.toDomain():MovieItem {
    return MovieItem(
        adult,
        backdropPath,
        emptyList(),
        id,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        releaseDate,
        title,
        video,
        voteAverage,
        voteCount,
    )
}

fun MovieDetailModel.toDomain():MovieItem {

    val genreIds = genres.map { it.name }

    return MovieItem(
        adult,
        backdropPath,
        genreIds,
        id,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        releaseDate,
        title,
        video,
        voteAverage,
        voteCount,
        tagline,
    )
}
