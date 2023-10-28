package com.globant.imdb.data

import com.globant.imdb.data.model.Movie
import com.globant.imdb.data.model.MovieProvider
import com.globant.imdb.data.remote.retrofit.TMDBService

class IMDbRepository {
    private val api = TMDBService()
    suspend fun getNowPlayingMovies():List<Movie>{
        if(MovieProvider.movies.isEmpty()){
            val response = api.getNowPlayingMovies()?.results ?: emptyList()
            MovieProvider.movies = response
        }
        return MovieProvider.movies
    }
    suspend fun getUpcomingMovies():List<Movie>{
        val response = api.getUpcomingMovies()?.results ?: emptyList()
        return response
    }
    suspend fun getPopularMovies():List<Movie>{
        val response = api.getPopularMovies()?.results ?: emptyList()
        return response
    }
}