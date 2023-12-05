package com.globant.imdb.domain.model

import com.globant.imdb.data.database.entities.movie.MovieDetailEntity
import com.globant.imdb.data.model.movies.CollectionModel
import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.imdb.data.model.movies.SpokenLanguageModel

data class MovieDetailItem(
    val adult: Boolean?,
    val belongsToCollection: CollectionModel?,
    val backdropPath: String?,
    val budget: Int?,
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
    val spokenLanguages: List<SpokenLanguageModel>? = emptyList(),
    val status: String? = "",
    val tagline:String? = "",
    val title: String? = "",
    val video: Boolean? = false,
    val voteAverage: Double? = 0.0,
    val voteCount: Int? = 0
)

fun MovieDetailEntity.toDomain():MovieDetailItem {
    return MovieDetailItem(
        adult = adult,
        belongsToCollection = belongsToCollection,
        backdropPath = backdropPath,
        budget = budget,
        genres = genres?.map { it.toDomain() },
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = posterPath,
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun MovieDetailModel.toDomain():MovieDetailItem {
    return MovieDetailItem(
        adult = adult,
        belongsToCollection = belongsToCollection,
        backdropPath = backdropPath,
        budget = budget,
        genres = genres?.map { it.toDomain() },
        homepage = homepage,
        id = id,
        imdbId = imdbId,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = posterPath,
        revenue = revenue,
        runtime = runtime,
        spokenLanguages = spokenLanguages,
        status = status,
        tagline = tagline,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}
