package com.globant.imdb.data

import android.content.Context
import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.data.model.movies.MovieProvider
import com.globant.imdb.data.model.movies.Video
import com.globant.imdb.data.model.user.User
import com.globant.imdb.data.remote.firebase.FirestoreManager
import com.globant.imdb.data.remote.retrofit.TMDBService
import javax.inject.Inject

class IMDbRepository @Inject constructor() {
    companion object {
        val firestoreManager = FirestoreManager()
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
    fun setHandleFailure(
        handleFailure:(title:String, msg:String)->Unit
    ){
        firestoreManager.handleFailure = handleFailure
    }

    fun createUser(user:User, handleSuccess:(user:User?)->Unit){
        firestoreManager.createUser(user, handleSuccess)
    }

    fun getUser(context: Context, localEmail:String, handleSuccess:(user:User?)->Unit){
        firestoreManager.getUser(context, localEmail , handleSuccess)
    }

    fun getUserMoviesList(context: Context, numberList:Int, handleSuccess:(List<Movie>)->Unit){
        return firestoreManager.getUserMoviesList(context, numberList, handleSuccess)
    }

    fun addMovieToList(context: Context, movie:Movie, listNumber: Int, handleSuccess:(Movie)->Unit){
        return firestoreManager.addMovieToList(context, movie, listNumber, handleSuccess)
    }

    fun deleteMovieFromList(
        context: Context,
        movieId:Int,
        listNumber:Int,
        handleSuccess:()->Unit
    ){
        firestoreManager.deleteMovieFromList(context, movieId, listNumber, handleSuccess)
    }
}