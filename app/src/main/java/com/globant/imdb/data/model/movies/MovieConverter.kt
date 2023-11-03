package com.globant.imdb.data.model.movies

class MovieConverter {
    companion object{
        fun movieDetailToMovie(movieDetail: MovieDetail): Movie {
            val genreIds = movieDetail.genres.map { it.id }
            return Movie(
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