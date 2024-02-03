package com.globant.movies.datasource.local.room

import com.globant.common.CategoryType
import com.globant.common.SyncState
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
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RoomDataSourceTest {
    @MockK
    lateinit var movieDao: MovieDao
    @MockK
    lateinit var categoryMovieDao: CategoryMovieDao
    @MockK
    lateinit var syncCategoryMovieDao: SyncCategoryMovieDao
    @MockK
    lateinit var movieDetailDao: MovieDetailDao
    @MockK
    lateinit var categoryDao: CategoryDao

    @OptIn(ExperimentalCoroutinesApi::class)
    private var testCoroutineDispatcher = UnconfinedTestDispatcher()

    private lateinit var roomDataSource: RoomDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        coEvery { categoryDao.getAllCategories() } returns mockk(relaxed = true)
        roomDataSource = RoomDataSource(
            movieDao,
            categoryDao,
            categoryMovieDao,
            syncCategoryMovieDao,
            movieDetailDao,
            testCoroutineDispatcher
        )
    }

    @Test
    fun `getMoviesByCategory should call getMoviesByCategory from movieDao`() {
        runTest {
            //Given
            val category = CategoryType.POPULAR_MOVIES
            val expectedMovies = mockk<List<MovieEntity>>(relaxed = true)

            coEvery { movieDao.getMoviesByCategory(category) } returns expectedMovies

            //When
            roomDataSource.getMoviesByCategory(category)

            //Then
            coVerify { movieDao.getMoviesByCategory(category) }
        }
    }

    @Test
    fun `getMovieById should call getMovieDetailById from movieDetailDao at first time`() {
        runTest {
            //Given
            val movieId = 123456
            val expectedMovieDetail = mockk<MovieDetailEntity>(relaxed = true)

            coEvery { movieDetailDao.getMovieDetailById(movieId) } returns expectedMovieDetail

            //When
            roomDataSource.getMovieById(movieId)

            //Then
            coVerify { movieDetailDao.getMovieDetailById(movieId) }
        }
    }

    @Test
    fun `getMovieById should call getMovieById from movieDao if getMovieDetailById from movieDetailDao is null`() {
        runTest {
            //Given
            val movieId = 123456
            val expectedMovieDetail:MovieDetailEntity? = null
            val expectedMovie = mockk<MovieEntity>(relaxed = true)

            coEvery { movieDetailDao.getMovieDetailById(movieId) } returns expectedMovieDetail
            coEvery { movieDao.getMovieById(movieId) } returns expectedMovie

            //When
            roomDataSource.getMovieById(movieId)

            //Then
            coVerify { movieDetailDao.getMovieDetailById(movieId) }
        }
    }

    @Test
    fun `addMoviesToCategory should call addMoviesToCategory from categoryMovieDao`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movies = mockk<List<MovieEntity>>(relaxed = true)
            val moviesCategory = movies.map { it.toCategoryMovie(category) }

            coEvery { movieDao.insertMovieList(movies) } just Runs
            coEvery { categoryMovieDao.addMoviesToCategory(moviesCategory) } just Runs

            //When
            roomDataSource.addMoviesToCategory(movies, category)

            //Then
            coVerify { categoryMovieDao.addMoviesToCategory(moviesCategory) }
        }
    }

    @Test
    fun `addMovieToCategory should call addMovieToCategory from categoryMovieDao`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val movieCategory = spyk(CategoryMovieEntity(movieId, category))

            coEvery { categoryMovieDao.addMovieToCategory(movieCategory) } just Runs

            //When
            roomDataSource.addMovieToCategory(movieId, category)

            //Then
            coVerify { categoryMovieDao.addMovieToCategory(movieCategory) }
        }
    }

    @Test
    fun `deleteMovieFromCategory should call deleteMovieFromCategory from categoryMovieDao`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456

            coEvery { categoryMovieDao.deleteMovieFromCategory(movieId, category) } just Runs

            //When
            roomDataSource.deleteMovieFromCategory(movieId, category)

            //Then
            coVerify { categoryMovieDao.deleteMovieFromCategory(movieId, category) }
        }
    }

    @Test
    fun `deleteMoviesByCategory should call deleteMoviesByCategory from movieDao`() {
        runTest {
            //Given
            val category = CategoryType.UPCOMING_MOVIES

            coEvery { movieDao.deleteMoviesByCategory(category) } just Runs

            //When
            roomDataSource.deleteMoviesByCategory(category)

            //Then
            coVerify { movieDao.deleteMoviesByCategory(category) }
        }
    }

    @Test
    fun `addMovieToSync should call addMovieToSync from syncCategoryMovieDao`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val state = SyncState.PENDING_TO_ADD
            val syncCategoryMovie = spyk(SyncCategoryMovieEntity(movieId, category, state))

            coEvery { syncCategoryMovieDao.addMovieToSync(syncCategoryMovie) } just Runs

            //When
            roomDataSource.addMovieToSync(movieId, category, state)

            //Then
            coVerify { syncCategoryMovieDao.addMovieToSync(syncCategoryMovie) }
        }
    }

    @Test
    fun `getMoviesToSync should call getMoviesToSync from syncCategoryMovieDao`() {
        runTest {
            //Given
            val state = SyncState.PENDING_TO_ADD
            val expectedValue = mockk<List<SyncCategoryMovieEntity>>(relaxed = true)

            coEvery { syncCategoryMovieDao.getMoviesToSync(state) } returns expectedValue

            //When
            roomDataSource.getMoviesToSync(state)

            //Then
            coVerify {  syncCategoryMovieDao.getMoviesToSync(state) }
        }
    }

    @Test
    fun `deleteMovieFromSync should call deleteMovieFromSync from syncCategoryMovieDao`() {
        runTest {
            //Given
            val movieId = 123456
            val category = CategoryType.HISTORY_MOVIES

            coEvery { syncCategoryMovieDao.deleteMovieFromSync(movieId, category) } just Runs

            //When
            roomDataSource.deleteMovieFromSync(movieId, category)

            //Then
            coVerify { syncCategoryMovieDao.deleteMovieFromSync(movieId, category) }
        }
    }

    @Test
    fun `addMovieDetailList should call addMovieDetailList from movieDetailDao`() {
        runTest {
            //Given
            val movie = mockk<MovieDetailEntity>(relaxed = true)

            coEvery { movieDetailDao.insertMovieDetail(movie) } just Runs

            //When
            roomDataSource.addMovieDetailList(movie)

            //Then
            coVerify { movieDetailDao.insertMovieDetail(movie) }
        }
    }
}