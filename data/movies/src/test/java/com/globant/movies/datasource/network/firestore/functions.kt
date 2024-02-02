package com.globant.movies.datasource.network.firestore

import com.globant.movies.model.MovieItem

fun MovieItem.toMap(): Map<String, Any?> {
    return mutableMapOf(
        "adult" to adult,
        "backdropPath" to backdropPath,
        "id" to id,
        "originalLanguage" to originalLanguage,
        "originalTitle" to originalTitle,
        "overview" to overview,
        "popularity" to popularity,
        "posterPath" to posterPath,
        "releaseDate" to releaseDate,
        "title" to title,
        "video" to video,
        "voteAverage" to voteAverage,
        "voteCount" to voteCount
    )
}