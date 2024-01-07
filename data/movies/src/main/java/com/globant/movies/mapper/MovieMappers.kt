package com.globant.movies.mapper

import com.globant.common.CategoryType
import com.globant.movies.model.movies.GenreModel
import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.movies.model.movies.MovieModel
import com.globant.movies.model.movies.VideoModel
import com.globant.movies.datasource.local.room.entities.CategoryEntity
import com.globant.movies.datasource.local.room.entities.CategoryMovieEntity
import com.globant.movies.model.VideoItem
import com.globant.movies.datasource.local.room.entities.MovieDetailEntity
import com.globant.movies.datasource.local.room.entities.MovieEntity
import com.globant.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import com.globant.movies.datasource.local.room.entities.VideoEntity
import com.globant.movies.model.GenreItem
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem

fun GenreModel.toDomain(): GenreItem = GenreItem(id, name)

fun SyncCategoryMovieItem.toDatabase(): SyncCategoryMovieEntity{
    return SyncCategoryMovieEntity(idMovie, idCategory, syncState)
}

fun SyncCategoryMovieEntity.toDomain(): SyncCategoryMovieItem {
    return SyncCategoryMovieItem(idMovie, idCategory, syncState)
}

fun MovieEntity.toDomain(): MovieItem {
    return MovieItem(
        adult = adult,
        backdropPath = backdropPath,
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

fun MovieModel.toDomain(): MovieItem {
    return MovieItem(
        adult = adult,
        backdropPath = backdropPath,
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

fun MovieEntity.toDetail(): MovieDetailItem {
    return MovieDetailItem(
        adult = adult,
        backdropPath = backdropPath,
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

fun MovieDetailEntity.toDomain(): MovieDetailItem {
    return MovieDetailItem(
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
}

fun MovieDetailModel.toDomain(): MovieDetailItem {
    return MovieDetailItem(
        adult = adult,
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
}

fun VideoModel.toDomain(): VideoItem {
    return VideoItem(
        id,
        iso6391,
        iso31661,
        name,
        key,
        site,
        size,
        type,
        official,
        publishedAt
    )
}

fun VideoEntity.toDomain(): VideoItem {
    return VideoItem(
        id,
        iso6391,
        iso31661,
        name,
        key,
        site,
        size,
        type,
        official,
        publishedAt
    )
}

fun CategoryType.toDatabase(): CategoryEntity {
    return CategoryEntity(this)
}

fun MovieEntity.toCategoryMovie(category: CategoryType): CategoryMovieEntity {
    return CategoryMovieEntity(id, category)
}

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

fun MovieItem.toDatabase(): MovieEntity =
    MovieEntity(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
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

