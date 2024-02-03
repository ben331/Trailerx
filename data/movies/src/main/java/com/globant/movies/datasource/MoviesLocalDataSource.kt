package com.globant.movies.datasource

import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.movies.datasource.local.room.entities.MovieDetailEntity
import com.globant.movies.datasource.local.room.entities.MovieEntity
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem

interface MoviesLocalDataSource {
    suspend fun getMoviesByCategory(category:CategoryType):List<MovieItem>?
    suspend fun getMovieById(movieId: Int): MovieDetailItem?
    suspend fun addMoviesToCategory(movies:List<MovieEntity>, category: CategoryType): Boolean
    suspend fun addMovieToCategory(movieId:Int, category: CategoryType): Boolean
    suspend fun deleteMovieFromCategory(movieId:Int, category: CategoryType): Boolean
    suspend fun addMovieToSync(movieId:Int, category: CategoryType, state: SyncState): Boolean
    suspend fun deleteMovieFromSync(movieId:Int, category: CategoryType): Boolean
    suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>?
    suspend fun addMovieDetailList(movie: MovieDetailEntity): Boolean
    suspend fun deleteMoviesByCategory(category:CategoryType): Boolean
}

