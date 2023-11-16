package com.globant.imdb.data

import com.globant.imdb.data.model.movies.MovieModel
import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.imdb.data.model.movies.MovieProvider
import com.globant.imdb.data.model.movies.VideoModel
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.data.remote.firebase.FirestoreManager
import com.globant.imdb.data.remote.retrofit.TMDBService
import javax.inject.Inject

class IMDbRepository @Inject constructor(
    private val api:TMDBService,
    private val firestoreManager:FirestoreManager
) {
    suspend fun getNowPlayingMovies():List<MovieModel>{
        if(MovieProvider.movies.isEmpty()){
            val response = api.getNowPlayingMovies()?.results ?: emptyList()
            MovieProvider.movies = response
        }
        return MovieProvider.movies
    }
    suspend fun getUpcomingMovies():List<MovieModel>{
        val response = api.getUpcomingMovies()?.results ?: emptyList()
        return response
    }
    suspend fun getPopularMovies():List<MovieModel>{
        val response = api.getPopularMovies()?.results ?: emptyList()
        return response
    }
    suspend fun getMovieById(movieId:Int): MovieDetailModel?{
        val response = api.getMovieById(movieId)
        return response
    }
    suspend fun searchMovie(query:String):List<MovieModel>{
        val response = api.searchMovie(query)?.results ?: emptyList()
        return response
    }
    suspend fun getTrailers(movieId:Int):List<VideoModel>{
        val response = api.getTrailers(movieId)?.results ?: emptyList()
        return response
    }

    // Firestore
    fun createUser(user:UserModel, handleSuccess:(user:UserModel?)->Unit){
        firestoreManager.createUser(user, handleSuccess)
    }

    fun getUser(
        localEmail:String,
        handleSuccess:(user:UserModel?)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        firestoreManager.getUser(localEmail , handleSuccess, handleFailure)
    }

    fun getUserMoviesList(
        numberList:Int,
        handleSuccess:(List<MovieModel>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit)
    {
        return firestoreManager.getUserMoviesList(numberList, handleSuccess, handleFailure)
    }

    fun addMovieToList(
        movie:MovieModel,
        listNumber: Int,
        handleSuccess:(MovieModel)->Unit,
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