package com.globant.imdb.data.repositories

import com.globant.imdb.data.database.dao.movie.MovieDao
import com.globant.imdb.data.database.dao.movie.CategoryDao
import com.globant.imdb.data.database.dao.movie.CategoryMovieDao
import com.globant.imdb.data.database.entities.movie.MovieEntity
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.toCategoryMovie
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
import javax.inject.Singleton
@Singleton
class IMDbRepository @Inject constructor(
    private val api:TMDBService,
    private val firestoreManager:FirestoreManager,
    private val movieDao: MovieDao,
    private val categoryDao: CategoryDao,
    private val categoryMovieDao: CategoryMovieDao,
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            val moviesLists = categoryDao.getAllCategories()
            if(moviesLists.isEmpty()){
                categoryDao.insertAll( CategoryType.values().map { it.toDatabase() } )
            }
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
    suspend fun getMoviesByCategoryFromDatabase(category:CategoryType):List<MovieItem>{
        val response = movieDao.getMoviesByCategory(category.name)
        return response.map { it.toDomain() }
    }
    suspend fun insertMoviesToCategory(movies:List<MovieEntity>, category: CategoryType){
        movieDao.insertMovieList(movies)
        categoryMovieDao.addMoviesToCategory( movies.map{ it.toCategoryMovie(category) } )
    }
    suspend fun getMovieByIdFromDatabase(movieId:Int): MovieItem?{
        val response = movieDao.getMovieById(movieId)
        return response?.toDomain()
    }
    suspend fun updateMovie(movie:MovieEntity){
        movieDao.updateMovie(movie)
    }
    suspend fun clearMoviesByCategory(category: CategoryType){
        movieDao.deleteMoviesByCategory(category.name)
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
        listType:CategoryType,
        handleSuccess:(List<MovieItem>)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        return firestoreManager.getUserMoviesList(listType, handleSuccess, handleFailure)
    }

    fun addMovieToList(
        movie:MovieItem,
        listType:CategoryType,
        handleSuccess:(MovieItem)->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        return firestoreManager.addMovieToList(movie, listType, handleSuccess, handleFailure)
    }

    fun deleteMovieFromList(
        movieId:Int,
        listType:CategoryType,
        handleSuccess:()->Unit,
        handleFailure:(title:Int, msg:Int)->Unit
    ){
        firestoreManager.deleteMovieFromList(movieId, listType, handleSuccess, handleFailure)
    }
}