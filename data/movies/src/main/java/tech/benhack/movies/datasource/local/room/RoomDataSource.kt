package tech.benhack.movies.datasource.local.room

import tech.benhack.common.CategoryType
import tech.benhack.common.SyncState
import tech.benhack.di.DefaultDispatcher
import tech.benhack.movies.datasource.MoviesLocalDataSource
import tech.benhack.movies.datasource.local.room.dao.CategoryDao
import tech.benhack.movies.datasource.local.room.dao.CategoryMovieDao
import tech.benhack.movies.datasource.local.room.dao.MovieDao
import tech.benhack.movies.datasource.local.room.dao.MovieDetailDao
import tech.benhack.movies.datasource.local.room.dao.SyncCategoryMovieDao
import tech.benhack.movies.datasource.local.room.entities.CategoryMovieEntity
import tech.benhack.movies.datasource.local.room.entities.MovieDetailEntity
import tech.benhack.movies.datasource.local.room.entities.MovieEntity
import tech.benhack.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import tech.benhack.movies.mapper.toCategoryMovie
import tech.benhack.movies.mapper.toDatabase
import tech.benhack.movies.mapper.toDetail
import tech.benhack.movies.mapper.toDomain
import tech.benhack.movies.model.MovieDetailItem
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.model.SyncCategoryMovieItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val movieDao: MovieDao,
    private val categoryDao: CategoryDao,
    private val categoryMovieDao: CategoryMovieDao,
    private val syncCategoryMovieDao: SyncCategoryMovieDao,
    private val movieDetailDao: MovieDetailDao,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher
):MoviesLocalDataSource {

    init {
        CoroutineScope(defaultDispatcher).launch {
            try {
                val moviesLists = categoryDao.getAllCategories()
                if(moviesLists.isEmpty()){
                    categoryDao.insertAll( CategoryType.values().map { it.toDatabase() } )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getMoviesByCategory(category: CategoryType): List<MovieItem>? {
        return try {
            movieDao.getMoviesByCategory(category).map { it.toDomain() }
        } catch (e:Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getMovieById(movieId: Int): MovieDetailItem? {
        return try {
            var response = movieDetailDao.getMovieDetailById(movieId)?.toDomain()
            if(response == null) {
                response = movieDao.getMovieById(movieId)?.toDetail()
            }
            return response
        } catch (e:Exception) {
            e.printStackTrace()
            null
        }

    }

    override suspend fun addMoviesToCategory(movies: List<MovieEntity>, category: CategoryType): Boolean {
        return try {
            movieDao.insertMovieList(movies)
            categoryMovieDao.addMoviesToCategory( movies.map{ it.toCategoryMovie(category) } )
            true
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun addMovieToCategory(movieId: Int, category: CategoryType): Boolean {
        return try {
            categoryMovieDao.addMovieToCategory(CategoryMovieEntity(movieId, category))
            true
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteMovieFromCategory(
        movieId: Int,
        category: CategoryType
    ): Boolean {
        return try {
            categoryMovieDao.deleteMovieFromCategory(movieId, category)
            true
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteMoviesByCategory(category: CategoryType): Boolean {
        return try {
            movieDao.deleteMoviesByCategory(category)
            true
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun addMovieToSync(movieId: Int, category: CategoryType, state: SyncState):Boolean {
        return try {
            syncCategoryMovieDao.addMovieToSync(SyncCategoryMovieEntity(movieId, category, state))
            true
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getMoviesToSync(state: SyncState): List<SyncCategoryMovieItem>? {
        return try {
            syncCategoryMovieDao.getMoviesToSync(state).map { it.toDomain() }
        } catch (e:Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun deleteMovieFromSync(movieId: Int, category: CategoryType): Boolean {
        return try {
            syncCategoryMovieDao.deleteMovieFromSync(movieId, category)
            true
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun addMovieDetailList(movie: MovieDetailEntity):Boolean {
        return try {
            movieDetailDao.insertMovieDetail(movie)
            true
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }
}