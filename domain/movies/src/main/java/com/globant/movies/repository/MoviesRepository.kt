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

interface MoviesRepository {

    val isServiceAvailable: Flow<Boolean>

    //-----API--------------------------------------------------------------------------------
    suspend fun getNowPlayingMoviesFromApi():List<MovieItem>
    suspend fun getUpcomingMoviesFromApi():List<MovieItem>
    suspend fun getPopularMoviesFromApi():List<MovieItem>
    suspend fun getMovieByIdFromApi(movieId:Int): MovieDetailItem?
    suspend fun searchMovie(query:String):List<MovieItem>
    suspend fun getTrailersFromApi(movieId:Int):List<VideoItem>
    suspend fun getUserMoviesList(listType:CategoryType, email:String): Either<ErrorData, List<MovieItem>>
    suspend fun addMovieToCategory(movie:MovieItem, category:CategoryType, email:String):Boolean
    suspend fun deleteMovieFromCategory(movieId:Int, category:CategoryType, email:String):Boolean

    //------DB-----------------------------------------------------------------
    suspend fun getMoviesByCategoryFromLocal(category:CategoryType): Either<ErrorData, List<MovieItem>>
    suspend fun addMoviesToCategoryLocal(movies:List<MovieItem>, category: CategoryType): Boolean
    suspend fun addMovieToCategoryLocal(movieId:Int, category: CategoryType): Boolean
    suspend fun deleteMovieFromCategoryLocal(movieId:Int, category: CategoryType): Boolean
    suspend fun addMovieToSyncLocal(movieId:Int, category: CategoryType, state:SyncState): Boolean
    suspend fun deleteMovieFromSyncLocal(movieId:Int, category: CategoryType): Boolean
    suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>?
    suspend fun getMovieByIdFromLocal(movieId:Int): MovieDetailItem?
    suspend fun addMovieDetailLocal(movie:MovieDetailItem): Boolean
    suspend fun clearMoviesByCategoryLocal(category: CategoryType): Boolean
}

