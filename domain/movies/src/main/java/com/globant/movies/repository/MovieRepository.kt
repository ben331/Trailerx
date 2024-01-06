package com.globant.movies.repository

import com.globant.common.CategoryType
import com.globant.common.Either
import com.globant.common.ErrorData
import com.globant.common.SyncState
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem
import com.globant.movies.model.VideoItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    val isServiceAvailable: Flow<Boolean>

    //-----API--------------------------------------------------------------------------------
    suspend fun getNowPlayingMoviesFromApi():List<MovieItem>
    suspend fun getUpcomingMoviesFromApi():List<MovieItem>
    suspend fun getPopularMoviesFromApi():List<MovieItem>
    suspend fun getMovieByIdFromApi(movieId:Int): MovieDetailItem?
    suspend fun searchMovie(query:String):List<MovieItem>
    suspend fun getTrailersFromApi(movieId:Int):List<VideoItem>
    suspend fun getUserMoviesList(listType:CategoryType): Either<ErrorData, List<MovieItem>>
    suspend fun addMovieToCategory(movie:MovieItem, category:CategoryType):Boolean
    suspend fun deleteMovieFromCategory(movieId:Int, category:CategoryType):Boolean

    //------DB-----------------------------------------------------------------
    suspend fun getMoviesByCategoryFromDatabase(category:CategoryType): Either<ErrorData, List<MovieItem>>
    suspend fun addMoviesToCategoryDatabase(movies:List<MovieItem>, category: CategoryType)
    suspend fun addMovieToCategoryDatabase(movieId:Int, category: CategoryType)
    suspend fun deleteMovieFromCategoryDatabase(movieId:Int, category: CategoryType)
    suspend fun addMovieToSyncDatabase(movieId:Int, category: CategoryType, state:SyncState)
    suspend fun deleteMovieFromSyncDatabase(movieId:Int, category: CategoryType)
    suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>
    suspend fun getMovieByIdFromDatabase(movieId:Int): MovieDetailItem?
    suspend fun addMovieDetailDatabase(movie:MovieDetailItem)
    suspend fun clearMoviesByCategoryDatabase(category: CategoryType)
}