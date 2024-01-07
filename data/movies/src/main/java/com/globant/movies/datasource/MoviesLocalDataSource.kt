package com.globant.movies.datasource

import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.movies.datasource.local.room.entities.MovieDetailEntity
import com.globant.movies.datasource.local.room.entities.MovieEntity
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem

interface MoviesLocalDataSource {
    suspend fun getMoviesByCategory(category:CategoryType):List<MovieItem>
    suspend fun getMovieById(movieId: Int): MovieDetailItem?
    suspend fun getUserMoviesCategory(listType: CategoryType):List<MovieItem>
    suspend fun addMoviesToCategory(movies:List<MovieEntity>, category: CategoryType)
    suspend fun addMovieToCategory(movieId:Int, category: CategoryType)
    suspend fun deleteMovieFromCategory(movieId:Int, category: CategoryType)
    suspend fun addMovieToSync(movieId:Int, category: CategoryType, state: SyncState)
    suspend fun deleteMovieFromSync(movieId:Int, category: CategoryType)
    suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>
    suspend fun addMovieDetailList(movie: MovieDetailEntity)
    suspend fun deleteMoviesByCategory(category:CategoryType)
}

