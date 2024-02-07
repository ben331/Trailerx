package tech.benhack.movies.datasource

import tech.benhack.movies.model.MovieDetailItem
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.model.VideoItem

interface MoviesNetworkDataSource {

    suspend fun testService():Boolean
    suspend fun getNowPlayingMovies(): List<MovieItem>?
    suspend fun getUpcomingMovies(): List<MovieItem>?
    suspend fun getPopularMovies(): List<MovieItem>?
    suspend fun getMovieById(movieId: Int): MovieDetailItem?
    suspend fun searchMovie(query: String): List<MovieItem>?
    suspend fun getTrailers(movieId: Int): List<VideoItem>?
}