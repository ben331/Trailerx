package tech.benhack.movies.mapper

import tech.benhack.common.CategoryType
import tech.benhack.movies.model.movies.GenreModel
import tech.benhack.trailerx.data.model.movies.MovieDetailModel
import tech.benhack.movies.model.movies.MovieModel
import tech.benhack.movies.model.movies.VideoModel
import tech.benhack.movies.datasource.local.room.entities.CategoryEntity
import tech.benhack.movies.datasource.local.room.entities.CategoryMovieEntity
import tech.benhack.movies.model.VideoItem
import tech.benhack.movies.datasource.local.room.entities.MovieDetailEntity
import tech.benhack.movies.datasource.local.room.entities.MovieEntity
import tech.benhack.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import tech.benhack.movies.datasource.local.room.entities.VideoEntity
import tech.benhack.movies.model.GenreItem
import tech.benhack.movies.model.MovieDetailItem
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.model.SyncCategoryMovieItem

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
        trailerxId = trailerxId,
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
        trailerxId = trailerxId,
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
        trailerxId = trailerxId,
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

