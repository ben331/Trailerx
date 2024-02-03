package com.globant.movies.datasource.local.room

import com.globant.common.CategoryType
import com.globant.common.SyncState
import com.globant.di.DefaultDispatcher
import com.globant.movies.datasource.MoviesLocalDataSource
import com.globant.movies.datasource.local.room.dao.CategoryDao
import com.globant.movies.datasource.local.room.dao.CategoryMovieDao
import com.globant.movies.datasource.local.room.dao.MovieDao
import com.globant.movies.datasource.local.room.dao.MovieDetailDao
import com.globant.movies.datasource.local.room.dao.SyncCategoryMovieDao
import com.globant.movies.datasource.local.room.entities.CategoryMovieEntity
import com.globant.movies.datasource.local.room.entities.MovieDetailEntity
import com.globant.movies.datasource.local.room.entities.MovieEntity
import com.globant.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import com.globant.movies.mapper.toCategoryMovie
import com.globant.movies.mapper.toDatabase
import com.globant.movies.mapper.toDetail
import com.globant.movies.mapper.toDomain
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RoomDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val categoryDao: CategoryDao,
    private val categoryMovieDao: CategoryMovieDao,
    private val syncCategoryMovieDao: SyncCategoryMovieDao,
    private val movieDetailDao: MovieDetailDao,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineContext
):MoviesLocalDataSource {

    init {
        CoroutineScope(defaultDispatcher).launch {
            val moviesLists = categoryDao.getAllCategories()
            if(moviesLists.isEmpty()){
                categoryDao.insertAll( CategoryType.values().map { it.toDatabase() } )
            }
        }
    }

    override suspend fun getMoviesByCategory(category: CategoryType): List<MovieItem> =
        movieDao.getMoviesByCategory(category).map { it.toDomain() }

    override suspend fun getMovieById(movieId: Int): MovieDetailItem? {
        var response = movieDetailDao.getMovieDetailById(movieId)?.toDomain()
        if(response == null) {
            response = movieDao.getMovieById(movieId)?.toDetail()
        }
        return response
    }

    override suspend fun addMoviesToCategory(movies: List<MovieEntity>, category: CategoryType) {
        movieDao.insertMovieList(movies)
        categoryMovieDao.addMoviesToCategory( movies.map{ it.toCategoryMovie(category) } )
    }

    override suspend fun addMovieToCategory(movieId: Int, category: CategoryType) =
        categoryMovieDao.addMovieToCategory(CategoryMovieEntity(movieId, category))

    override suspend fun deleteMovieFromCategory(
        movieId: Int,
        category: CategoryType
    ) = categoryMovieDao.deleteMovieFromCategory(movieId, category)

    override suspend fun deleteMoviesByCategory(category: CategoryType) =
        movieDao.deleteMoviesByCategory(category)

    override suspend fun addMovieToSync(movieId: Int, category: CategoryType, state: SyncState) =
        syncCategoryMovieDao.addMovieToSync(SyncCategoryMovieEntity(movieId, category, state))

    override suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem> =
        syncCategoryMovieDao.getMoviesToSync(state).map { it.toDomain() }

    override suspend fun deleteMovieFromSync(movieId: Int, category: CategoryType) =
        syncCategoryMovieDao.deleteMovieFromSync(movieId, category)

    override suspend fun addMovieDetailList(movie: MovieDetailEntity) =
        movieDetailDao.insertMovieDetail(movie)
}