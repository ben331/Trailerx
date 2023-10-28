package com.globant.imdb.data.remote.retrofit

import com.globant.imdb.data.model.MovieDetail
import com.globant.imdb.data.model.MoviesList
import com.globant.imdb.data.model.MoviesListDates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiClient {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesListDates?>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesListDates?>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesList?>

    @GET("movie/{movieId}")
    suspend fun getMovieById(
        @Path("movieId") movieId:Int,
        @Query("language") language: String,
    ):Response<MovieDetail?>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MoviesList?>

}