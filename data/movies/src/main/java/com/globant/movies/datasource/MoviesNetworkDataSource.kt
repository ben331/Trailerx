package com.globant.movies.datasource

import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.VideoItem

interface MoviesNetworkDataSource {

    suspend fun testService():Boolean
    suspend fun getNowPlayingMovies(): List<MovieItem>?
    suspend fun getUpcomingMovies(): List<MovieItem>?
    suspend fun getPopularMovies(): List<MovieItem>?
    suspend fun getMovieById(movieId: Int): MovieDetailItem?
    suspend fun searchMovie(query: String): List<MovieItem>?
    suspend fun getTrailers(movieId: Int): List<VideoItem>?
}