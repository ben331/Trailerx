package com.globant.movies.model

data class MovieDetailItem(
    val adult: Boolean? = false,
    val backdropPath: String?,
    val budget: Int? = 0,
    val genres: List<GenreItem>? = emptyList(),
    val homepage:String? = "",
    val id: Int = 0,
    val imdbId:String? = "",
    val originalLanguage: String? = "",
    val originalTitle: String? = "",
    val overview: String? = "",
    val popularity: Double? = 0.0,
    val posterPath: String? = "",
    val releaseDate: String? = "",
    val revenue: Int? = 0,
    val runtime:Int? = 0,
    val status: String? = "",
    val tagline:String? = "",
    val title: String? = "",
    val video: Boolean? = false,
    val voteAverage: Double? = 0.0,
    val voteCount: Int? = 0
)
