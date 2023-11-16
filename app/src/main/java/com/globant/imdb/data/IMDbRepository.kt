package com.globant.imdb.data

import com.globant.imdb.data.model.movies.Movie
import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.data.model.movies.MovieProvider
import com.globant.imdb.data.model.movies.Video
import com.globant.imdb.data.model.user.User
import com.globant.imdb.data.remote.firebase.FirestoreManager
import com.globant.imdb.data.remote.retrofit.TMDBService
import javax.inject.Inject

class IMDbRepository @Inject constructor(
    private val api:TMDBService,
    private val firestoreManager:FirestoreManager
) {
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
    fun createUser(user:User, handleSuccess:(user:User?)->Unit){
        firestoreManager.createUser(user, handleSuccess)
    }

    fun getUser(
        localEmail:String,
        handleSuccess:(user:User?)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        firestoreManager.getUser(localEmail , handleSuccess, handleFailure)
    }

    fun getUserMoviesList(
        numberList:Int,
        handleSuccess:(List<Movie>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit)
    {
        return firestoreManager.getUserMoviesList(numberList, handleSuccess, handleFailure)
    }

    fun addMovieToList(
        movie:Movie,
        listNumber: Int,
        handleSuccess:(Movie)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit)
    {
        return firestoreManager.addMovieToList(movie, listNumber, handleSuccess, handleFailure)
    }

    fun deleteMovieFromList(
        movieId:Int,
        listNumber:Int,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        firestoreManager.deleteMovieFromList(movieId, listNumber, handleSuccess, handleFailure)
    }
}