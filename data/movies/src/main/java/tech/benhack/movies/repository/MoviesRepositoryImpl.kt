package tech.benhack.movies.repository

import tech.benhack.common.CategoryType
import tech.benhack.common.Either
import tech.benhack.common.ErrorData
import tech.benhack.common.ErrorFactory
import tech.benhack.common.ErrorStatusCode
import tech.benhack.common.SyncState
import tech.benhack.movies.datasource.MoviesLocalDataSource
import tech.benhack.movies.datasource.MoviesNetworkDataSource
import tech.benhack.movies.datasource.UserMoviesNetworkDataSource
import tech.benhack.movies.mapper.toDatabase
import tech.benhack.movies.model.MovieDetailItem
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.model.SyncCategoryMovieItem
import tech.benhack.movies.model.VideoItem
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
            if(!response.isNullOrEmpty()){
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
    override suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>? =
        moviesLocalDataSource.getMoviesToSync(state)
    override suspend fun getMovieByIdFromLocal(movieId:Int): MovieDetailItem? =
        moviesLocalDataSource.getMovieById(movieId)
    override suspend fun addMovieDetailLocal(movie: MovieDetailItem) =
        moviesLocalDataSource.addMovieDetailList(movie.toDatabase())
    override suspend fun clearMoviesByCategoryLocal(category: CategoryType): Boolean =
        moviesLocalDataSource.deleteMoviesByCategory(category)
}