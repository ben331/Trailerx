package com.globant.imdb.data

import com.globant.imdb.data.database.dao.movie.MovieDao
import com.globant.imdb.data.database.dao.movie.MovieListDao
import com.globant.imdb.data.database.entities.movie.MovieEntity
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.data.network.firebase.FirestoreManager
import com.globant.imdb.data.network.retrofit.TMDBService
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.model.VideoItem
import com.globant.imdb.domain.model.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class IMDbRepository @Inject constructor(
    private val api:TMDBService,
    private val firestoreManager:FirestoreManager,
    private val movieDao: MovieDao,
    private val movieListDao: MovieListDao,
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            movieListDao.deleteAll()
            movieListDao.insertAll( MovieListType.values().map { it.toDatabase() } )
        }
    }

    //-----RETROFIT--------------------------------------------------------------------------------

    suspend fun getNowPlayingMoviesFromApi():List<MovieItem>{
        val response = api.getNowPlayingMovies()?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getUpcomingMoviesFromApi():List<MovieItem>{
        val response = api.getUpcomingMovies()?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getPopularMoviesFromApi():List<MovieItem>{
        val response = api.getPopularMovies()?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getMovieByIdFromApi(movieId:Int): MovieItem?{
        val response = api.getMovieById(movieId)
        return response?.toDomain()
    }
    suspend fun searchMovie(query:String):List<MovieItem>{
        val response = api.searchMovie(query)?.results ?: emptyList()
        return response.map { it.toDomain() }
    }
    suspend fun getTrailersFromApi(movieId:Int):List<VideoItem>{
        val response = api.getTrailers(movieId)?.results ?: emptyList()
        return response.map { it.toDomain() }
    }

    //-------ROOM [RETROFIT CACHE]-----------------------------------------------------------------
    suspend fun getNowPlayingMoviesFromDatabase():List<MovieItem>{
        val response = movieDao.getListOfMovies(MovieListType.NOW_PLAYING_MOVIES.name)
        return response.map { it.toDomain() }
    }
    suspend fun insertNowPlayingMovies(movies:List<MovieEntity>){
        movieDao.insertMovieList(movies)
    }
    suspend fun getUpcomingMoviesFromDatabase():List<MovieItem>{
        val response = movieDao.getListOfMovies(MovieListType.UPCOMING_MOVIES.name)
        return response.map { it.toDomain() }
    }
    suspend fun insertUpcomingMovies(movies:List<MovieEntity>){
        movieDao.insertMovieList(movies)
    }
    suspend fun getPopularMoviesFromDatabase():List<MovieItem>{
        val response = movieDao.getListOfMovies(MovieListType.POPULAR_MOVIES.name)
        return response.map { it.toDomain() }
    }
    suspend fun insertPopularMovies(movies:List<MovieEntity>){
        movieDao.insertMovieList(movies)
    }
    suspend fun getMovieByIdFromDatabase(movieId:Int): MovieItem?{
        val response = movieDao.getMovieById(movieId)
        return response?.toDomain()
    }
    suspend fun updateMovieTagLine(id:Int, tagline:String){
        movieDao.updateTagLine(id, tagline)
    }
    suspend fun clearMovieList(listId:String){
        movieDao.deleteMovieList(listId)
    }


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
        listType:MovieListType,
        handleSuccess:(List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit)
    {
        return firestoreManager.getUserMoviesList(listType, handleSuccess, handleFailure)
    }

    fun addMovieToList(
        movie:MovieItem,
        listType:MovieListType,
        handleSuccess:(MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit)
    {
        return firestoreManager.addMovieToList(movie, listType, handleSuccess, handleFailure)
    }

    fun deleteMovieFromList(
        movieId:Int,
        listType:MovieListType,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        firestoreManager.deleteMovieFromList(movieId, listType, handleSuccess, handleFailure)
    }
}