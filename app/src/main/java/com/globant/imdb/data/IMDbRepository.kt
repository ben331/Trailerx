package com.globant.imdb.data

import com.globant.imdb.data.model.Movie
import com.globant.imdb.data.model.MovieProvider
import com.globant.imdb.data.remote.retrofit.TMDBService

class IMDbRepository {
    private val api = TMDBService()
    suspend fun getTopRatedMovies():List<Movie>{
        val response = api.getTopRatedMovies()?.results ?: emptyList()
        MovieProvider.movies = response
        return response
    }
}