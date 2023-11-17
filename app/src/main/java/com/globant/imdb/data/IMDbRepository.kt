package com.globant.imdb.data

import com.globant.imdb.data.model.movies.MovieModel
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.data.network.firebase.FirestoreManager
import com.globant.imdb.data.network.retrofit.TMDBService
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.model.VideoItem
import com.globant.imdb.domain.model.toDomain
import javax.inject.Inject

class IMDbRepository @Inject constructor(
    private val api:TMDBService,
    private val firestoreManager:FirestoreManager
) {
    //-----RETROFIT--------------------------------------------------------------------------------

    suspend fun getNowPlayingMovies():List<MovieItem>{
        val response = api.getNowPlayingMovies()?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getUpcomingMovies():List<MovieItem>{
        val response = api.getUpcomingMovies()?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getPopularMovies():List<MovieItem>{
        val response = api.getPopularMovies()?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getMovieById(movieId:Int): MovieItem?{
        val response = api.getMovieById(movieId)
        return response?.toDomain()
    }
    suspend fun searchMovie(query:String):List<MovieItem>{
        val response = api.searchMovie(query)?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getTrailers(movieId:Int):List<VideoItem>{
        val response = api.getTrailers(movieId)?.results ?: emptyList()
        return response.map { it.toDomain() }
    }

    //-------ROOM----------------------------------------------------------------------------------



    //-----FIRESTORE-------------------------------------------------------------------------------

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
        movie:MovieItem,
        listNumber: Int,
        handleSuccess:(MovieItem)->Unit,
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