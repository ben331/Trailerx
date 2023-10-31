package com.globant.imdb.data

import android.content.Context
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.data.model.movies.MovieProvider
import com.globant.imdb.data.model.movies.Video
import com.globant.imdb.data.remote.firebase.FirestoreManager
import com.globant.imdb.data.remote.retrofit.TMDBService

class IMDbRepository {
    companion object {
        lateinit var firestoreManager: FirestoreManager
    }

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
    suspend fun getMovieById(movieId:Int): MovieDetail?{
        val response = api.getMovieById(movieId)
        return response
    }
    suspend fun searchMovie(query:String):List<Movie>{
        val response = api.searchMovie(query)?.results ?: emptyList()
        return response
    }
    suspend fun getTrailers(movieId:Int):List<Video>{
        val response = api.getTrailers(movieId)?.results ?: emptyList()
        return response
    }

    // Firestore
    fun setupUserRepository(
        handleSuccessGetMovies:(movies:ArrayList<Movie>)->Unit,
        handleSuccessAddMovie:(movie:Movie)->Unit,
        handleFailure:(title:String, msg:String)->Unit
    ){
        firestoreManager = FirestoreManager.Builder()
            .setHandleSuccessGetMovies(handleSuccessGetMovies)
            .setHandleSuccessAddMovie(handleSuccessAddMovie)
            .setHandleFailure(handleFailure)
            .build()
    }

    fun getWatchList(context: Context){
        return firestoreManager.getWatchList(context)
    }

    fun addMovieToWatchList(context: Context, movie:Movie){
        return firestoreManager.addMovieToWatchList(context, movie)
    }
}