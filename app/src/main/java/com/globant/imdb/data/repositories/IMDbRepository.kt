package com.globant.imdb.data.repositories

import com.globant.imdb.core.ServiceMonitor
import com.globant.imdb.data.database.dao.movie.MovieDao
import com.globant.imdb.data.database.dao.movie.CategoryDao
import com.globant.imdb.data.database.dao.movie.CategoryMovieDao
import com.globant.imdb.data.database.dao.movie.MovieDetailDao
import com.globant.imdb.data.database.dao.movie.SyncCategoryMovieDao
import com.globant.imdb.data.database.entities.movie.CategoryMovieEntity
import com.globant.imdb.data.database.entities.movie.MovieEntity
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.MovieDetailEntity
import com.globant.imdb.data.database.entities.movie.SyncCategoryMovieEntity
import com.globant.imdb.data.database.entities.movie.SyncState
import com.globant.imdb.data.database.entities.movie.toCategoryMovie
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.data.model.user.UserModel
import com.globant.imdb.data.network.firebase.FirestoreManager
import com.globant.imdb.data.network.retrofit.TMDBService
import com.globant.imdb.domain.model.MovieDetailItem
import com.globant.imdb.domain.model.MovieItem
import com.globant.imdb.domain.model.SyncCategoryMovieItem
import com.globant.imdb.domain.model.VideoItem
import com.globant.imdb.domain.model.toDetail
import com.globant.imdb.domain.model.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_INTERVAL = 1000L

@Singleton
class IMDbRepository @Inject constructor(
    private val api:TMDBService,
    private val firestoreManager:FirestoreManager,
    private val movieDao: MovieDao,
    private val categoryDao: CategoryDao,
    private val categoryMovieDao: CategoryMovieDao,
    private val syncCategoryMovieDao: SyncCategoryMovieDao,
    private val movieDetailDao: MovieDetailDao,
    private val serviceMonitor: ServiceMonitor,
) {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            val moviesLists = categoryDao.getAllCategories()
            if(moviesLists.isEmpty()){
                categoryDao.insertAll( CategoryType.values().map { it.toDatabase() } )
            }
        }
    }

    val isConnectionAvailable: Flow<Boolean> = flow {
        while (true){
            try {
                emit(serviceMonitor.isConnected())
            }catch (e:Exception){
                emit(true)
            }finally {
                delay(REFRESH_INTERVAL)
            }
        }
    }.flowOn(Dispatchers.Default)

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
    suspend fun getMovieByIdFromApi(movieId:Int): MovieDetailItem?{
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
        val response = movieDao.getMoviesByCategory(category)
        return response.map { it.toDomain() }
    }
    suspend fun addMoviesToCategoryDatabase(movies:List<MovieEntity>, category: CategoryType){
        movieDao.insertMovieList(movies)
        categoryMovieDao.addMoviesToCategory( movies.map{ it.toCategoryMovie(category) } )
    }
    suspend fun addMovieToCategoryDatabase(movieId:Int, category: CategoryType){
        categoryMovieDao.addMovieToCategory( CategoryMovieEntity(movieId, category))
    }
    suspend fun deleteMovieFromCategoryDatabase(movieId:Int, category: CategoryType){
        categoryMovieDao.deleteMovieFromCategory(movieId, category)
    }
    suspend fun addMovieToSyncDatabase(movieId:Int, category: CategoryType, state:SyncState){
        syncCategoryMovieDao.addMovieToSync( SyncCategoryMovieEntity(movieId, category, state))
    }
    suspend fun deleteMovieFromSyncDatabase(movieId:Int, category: CategoryType){
        syncCategoryMovieDao.deleteMovieFromSync(movieId, category)
    }
    suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>{
        return syncCategoryMovieDao.getMoviesToSync(state).map { it.toDomain() }
    }
    suspend fun getMovieByIdFromDatabase(movieId:Int): MovieDetailItem?{
        var response = movieDetailDao.getMovieDetailById(movieId)?.toDomain()
        if(response == null) {
            response = movieDao.getMovieById(movieId)?.toDetail()
        }
        return response
    }
    suspend fun addMovieDetailDatabase(movie:MovieDetailEntity){
        movieDetailDao.insertMovieDetail(movie)
    }
    suspend fun clearMoviesByCategoryDatabase(category: CategoryType){
        movieDao.deleteMoviesByCategory(category)
    }


    //-----FIRESTORE-------------------------------------------------------------------------------
    suspend fun createUser(user:UserModel): Boolean {
        return firestoreManager.createUser(user)
    }

    suspend fun getUser(localEmail:String): UserModel? {
        return firestoreManager.getUser(localEmail)
    }

    suspend fun getUserMoviesList(listType:CategoryType): List<MovieItem>? {
        return firestoreManager.getUserMoviesList(listType)
    }

    suspend fun addMovieToCategory(movie:MovieItem, category:CategoryType):Boolean {
        return firestoreManager.addMovieToList(movie, category)
    }

    suspend fun deleteMovieFromCategory(movieId:Int, category:CategoryType):Boolean {
        return firestoreManager.deleteMovieFromList(movieId, category)
    }
}