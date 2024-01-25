package com.globant.movies.repository

import com.globant.common.CategoryType
import com.globant.common.Either
import com.globant.common.ErrorData
import com.globant.common.ErrorFactory
import com.globant.common.ErrorStatusCode
import com.globant.common.SyncState
import com.globant.movies.datasource.MoviesLocalDataSource
import com.globant.movies.datasource.MoviesNetworkDataSource
import com.globant.movies.datasource.UserMoviesNetworkDataSource
import com.globant.movies.mapper.toDatabase
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem
import com.globant.movies.model.VideoItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

private const val REFRESH_INTERVAL = 3000L

@Singleton
class MoviesRepositoryImpl @Inject constructor(
    private val moviesNetworkDataSource: MoviesNetworkDataSource,
    private val userMoviesNetworkDataSource: UserMoviesNetworkDataSource,
    private val moviesLocalDataSource: MoviesLocalDataSource,
):MoviesRepository {

    override val isServiceAvailable: Flow<Boolean> = flow {
        while (true) {
            val isConnected = moviesNetworkDataSource.testService()
            emit(isConnected)
            delay(REFRESH_INTERVAL)
        }
    }

    //-----API 1--------------------------------------------------------------------------------
    override suspend fun getNowPlayingMoviesFromApi(): List<MovieItem> =
        moviesNetworkDataSource.getNowPlayingMovies() ?: emptyList()
    override suspend fun getUpcomingMoviesFromApi():List<MovieItem> =
        moviesNetworkDataSource.getUpcomingMovies() ?: emptyList()
    override suspend fun getPopularMoviesFromApi():List<MovieItem> =
        moviesNetworkDataSource.getPopularMovies() ?: emptyList()
    override suspend fun getMovieByIdFromApi(movieId:Int): MovieDetailItem? =
        moviesNetworkDataSource.getMovieById(movieId)
    override suspend fun searchMovie(query:String):List<MovieItem> =
        moviesNetworkDataSource.searchMovie(query) ?: emptyList()
    override suspend fun getTrailersFromApi(movieId:Int):List<VideoItem> =
        moviesNetworkDataSource.getTrailers(movieId) ?: emptyList()

    //-------API 2 [USER DATA]-----------------------------------------------------------------
    override suspend fun getUserMoviesList(listType: CategoryType, email:String): Either<ErrorData, List<MovieItem>> {
        val movies = userMoviesNetworkDataSource.getUserMoviesList(listType, email)
        return if(movies!=null){
            Either.Right(movies)
        }else{
            Either.Left(ErrorFactory(ErrorStatusCode.NoInternetConnection.code))
        }
    }
    override suspend fun addMovieToCategory(movie: MovieItem, category: CategoryType, email:String):Boolean {
        return userMoviesNetworkDataSource.addMovieToList(movie, category, email)
    }
    override suspend fun deleteMovieFromCategory(movieId:Int, category: CategoryType, email:String):Boolean {
        return userMoviesNetworkDataSource.deleteMovieFromList(movieId, category, email)
    }

    //-------Local [CACHE]-----------------------------------------------------------------
    override suspend fun getMoviesByCategoryFromLocal(category: CategoryType): Either<ErrorData, List<MovieItem>> {
        return try {
            val response = moviesLocalDataSource.getMoviesByCategory(category)
            if(response.isNotEmpty()){
                Either.Right(response)
            }else{
                Either.Left(ErrorFactory(ErrorStatusCode.EmptyCache.code))
            }
        } catch ( e:Exception) {
            Either.Left(ErrorFactory(e.hashCode()))
        }
    }
    override suspend fun addMoviesToCategoryLocal(movies:List<MovieItem>, category: CategoryType) =
        moviesLocalDataSource.addMoviesToCategory(movies.map { it.toDatabase() }, category)
    override suspend fun addMovieToCategoryLocal(movieId:Int, category: CategoryType) =
        moviesLocalDataSource.addMovieToCategory(movieId, category)
    override suspend fun deleteMovieFromCategoryLocal(movieId:Int, category: CategoryType) =
        moviesLocalDataSource.deleteMovieFromCategory(movieId, category)
    override suspend fun addMovieToSyncLocal(movieId:Int, category: CategoryType, state: SyncState) =
        moviesLocalDataSource.addMovieToSync(movieId, category, state)
    override suspend fun deleteMovieFromSyncLocal(movieId:Int, category: CategoryType) =
        moviesLocalDataSource.deleteMovieFromSync(movieId, category)
    override suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem> =
        moviesLocalDataSource.getMoviesToSync(state)
    override suspend fun getMovieByIdFromLocal(movieId:Int): MovieDetailItem? =
        moviesLocalDataSource.getMovieById(movieId)
    override suspend fun addMovieDetailLocal(movie: MovieDetailItem) =
        moviesLocalDataSource.addMovieDetailList(movie.toDatabase())
    override suspend fun clearMoviesByCategoryLocal(category: CategoryType){
        moviesLocalDataSource.deleteMoviesByCategory(category)
    }
}