package com.globant.imdb.data.remote.retrofit

import com.globant.imdb.data.model.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApiClient {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ):Response<MovieList?>
}