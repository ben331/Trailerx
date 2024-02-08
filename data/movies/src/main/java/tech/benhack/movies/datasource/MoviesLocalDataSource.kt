package tech.benhack.movies.datasource

import tech.benhack.common.CategoryType
import tech.benhack.common.SyncState
import tech.benhack.movies.datasource.local.room.entities.MovieDetailEntity
import tech.benhack.movies.datasource.local.room.entities.MovieEntity
import tech.benhack.movies.model.MovieDetailItem
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.model.SyncCategoryMovieItem

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

