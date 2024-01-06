package com.globant.movies.repository

import com.globant.movies.datasource.local.room.entities.toCategoryMovie
import com.globant.movies.datasource.local.room.entities.toDatabase
import com.globant.movies.model.toDetail
import com.globant.movies.model.toDomain
import com.globant.movies.datasource.local.room.dao.CategoryDao
import com.globant.movies.datasource.local.room.dao.CategoryMovieDao
import com.globant.movies.dao.MovieDao
import com.globant.movies.datasource.local.room.dao.MovieDetailDao
import com.globant.movies.datasource.local.room.dao.SyncCategoryMovieDao
import com.globant.movies.datasource.local.room.entities.CategoryMovieEntity
import com.globant.movies.datasource.local.room.entities.CategoryType
import com.globant.movies.datasource.local.room.entities.MovieDetailEntity
import com.globant.movies.datasource.local.room.entities.MovieEntity
import com.globant.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import com.globant.movies.datasource.local.room.entities.SyncState
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem
import com.globant.movies.model.VideoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@javax.inject.Singleton
class MoviesRepositoryImpl @javax.inject.Inject constructor(
    private val api: com.globant.imdb.data.network.retrofit.TMDBService,
    private val firestoreManager: com.globant.imdb.data.network.firebase.FirestoreManager,
    private val movieDao: MovieDao,
    private val categoryDao: CategoryDao,
    private val categoryMovieDao: CategoryMovieDao,
    private val syncCategoryMovieDao: SyncCategoryMovieDao,
    private val movieDetailDao: MovieDetailDao,
    @com.globant.imdb.di.DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @com.globant.imdb.di.IoDispatcher ioDispatcher: CoroutineDispatcher
) {
    init {
        CoroutineScope(ioDispatcher).launch {
            val moviesLists = categoryDao.getAllCategories()
            if(moviesLists.isEmpty()){
                categoryDao.insertAll( CategoryType.values().map { com.globant.movies.entities.toDatabase() } )
            }
        }
    }

    val isServiceAvailable: Flow<Boolean> = flow {
        while (true) {
            val isConnected = api.testService()
            emit(isConnected)
            kotlinx.coroutines.delay(com.globant.imdb.data.repositories.REFRESH_INTERVAL)
        }
    }.flowOn(defaultDispatcher)

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
    suspend fun getMoviesByCategoryFromDatabase(category: CategoryType): com.globant.common.Either<com.globant.common.ErrorData, List<MovieItem>> {
        return try {
            val response = movieDao.getMoviesByCategory(category)
            if(response.isNotEmpty()){
                com.globant.common.Either.Right(response.map { it.toDomain() })
            }else{
                com.globant.common.Either.Left(com.globant.common.ErrorFactory(com.globant.common.ErrorStatusCode.EmptyCache.code))
            }
        } catch ( e:Exception) {
            com.globant.common.Either.Left(com.globant.common.ErrorFactory(e.hashCode()))
        }
    }
    suspend fun addMoviesToCategoryDatabase(movies:List<MovieEntity>, category: CategoryType){
        movieDao.insertMovieList(movies)
        categoryMovieDao.addMoviesToCategory( movies.map{ it.toCategoryMovie(category) } )
    }
    suspend fun addMovieToCategoryDatabase(movieId:Int, category: CategoryType){
        categoryMovieDao.addMovieToCategory(
            CategoryMovieEntity(
                movieId,
                category
            )
        )
    }
    suspend fun deleteMovieFromCategoryDatabase(movieId:Int, category: CategoryType){
        categoryMovieDao.deleteMovieFromCategory(movieId, category)
    }
    suspend fun addMovieToSyncDatabase(movieId:Int, category: CategoryType, state: SyncState){
        syncCategoryMovieDao.addMovieToSync(
            SyncCategoryMovieEntity(
                movieId,
                category,
                state
            )
        )
    }
    suspend fun deleteMovieFromSyncDatabase(movieId:Int, category: CategoryType){
        syncCategoryMovieDao.deleteMovieFromSync(movieId, category)
    }
    suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>{
        return syncCategoryMovieDao.getMoviesToSync(state).map { com.globant.movies.model.toDomain() }
    }
    suspend fun getMovieByIdFromDatabase(movieId:Int): MovieDetailItem?{
        var response = com.globant.movies.model.toDomain()
        if(response == null) {
            response = movieDao.getMovieById(movieId)?.toDetail()
        }
        return response
    }
    suspend fun addMovieDetailDatabase(movie: MovieDetailEntity){
        movieDetailDao.insertMovieDetail(movie)
    }
    suspend fun clearMoviesByCategoryDatabase(category: CategoryType){
        movieDao.deleteMoviesByCategory(category)
    }


    //-----FIRESTORE-------------------------------------------------------------------------------
    suspend fun createUser(user: com.globant.imdb.data.model.user.UserModel): Boolean {
        return firestoreManager.createUser(user)
    }

    suspend fun getUser(localEmail:String): com.globant.imdb.data.model.user.UserModel? {
        return firestoreManager.getUser(localEmail)
    }

    suspend fun getUserMoviesList(listType: CategoryType): com.globant.common.Either<com.globant.common.ErrorData, List<MovieItem>> {
        val movies = firestoreManager.getUserMoviesList(listType)
        return if(movies!=null){
            com.globant.common.Either.Right(movies)
        }else{
            com.globant.common.Either.Left(com.globant.common.ErrorFactory(com.globant.common.ErrorStatusCode.NoInternetConnection.code))
        }
    }

    suspend fun addMovieToCategory(movie: MovieItem, category: CategoryType):Boolean {
        return firestoreManager.addMovieToList(movie, category)
    }

    suspend fun deleteMovieFromCategory(movieId:Int, category: CategoryType):Boolean {
        return firestoreManager.deleteMovieFromList(movieId, category)
    }
}