package com.globant.imdb.data.model.movies

class MovieConverter {
    //TODO: CHANGE TO MAPPER
    companion object{
        fun movieDetailToMovie(movieDetail: MovieDetailModel): MovieModel {
            val genreIds = movieDetail.genres.map { it.id }
            return MovieModel(
                movieDetail.adult,
                movieDetail.backdropPath,
                genreIds,
                movieDetail.id,
                movieDetail.originalLanguage,
                movieDetail.originalTitle,
                movieDetail.overview,
                movieDetail.popularity,
                movieDetail.posterPath,
                movieDetail.releaseDate,
                movieDetail.title,
                movieDetail.video,
                movieDetail.voteAverage,
                movieDetail.voteCount,
            )
        }
    }
}