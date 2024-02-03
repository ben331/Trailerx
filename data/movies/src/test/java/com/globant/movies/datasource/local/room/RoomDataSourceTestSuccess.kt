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
import com.globant.movies.mapper.toDetail
import com.globant.movies.mapper.toDomain
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoomDataSourceTestSuccess {
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
    fun `getMoviesByCategory should return a List of MovieItem when call is success`() {
        runTest {
            //Given
            val category = CategoryType.POPULAR_MOVIES
            val expectedMovies = mockk<List<MovieEntity>>(relaxed = true)
            val expectedResult = expectedMovies.map { it.toDomain() }

            coEvery { movieDao.getMoviesByCategory(category) } returns expectedMovies

            //When
            val response = roomDataSource.getMoviesByCategory(category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getMovieById should return a MovieDetailItem when call is success from detail`() {
        runTest {
            //Given
            val movieId = 123456
            val expectedMovieDetail = mockk<MovieDetailEntity>(relaxed = true)
            val expectedResult = expectedMovieDetail.toDomain()

            coEvery { movieDetailDao.getMovieDetailById(movieId) } returns expectedMovieDetail

            //When
            val response = roomDataSource.getMovieById(movieId)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getMovieById should return a MovieDetailItem when call is error from detail but success from movie table`() {
        runTest {
            //Given
            val movieId = 123456
            val expectedMovieDetail:MovieDetailEntity? = null
            val expectedMovie = mockk<MovieEntity>(relaxed = true)
            val expectedResult = expectedMovie.toDetail()

            coEvery { movieDetailDao.getMovieDetailById(movieId) } returns expectedMovieDetail
            coEvery { movieDao.getMovieById(movieId) } returns expectedMovie

            //When
            val response = roomDataSource.getMovieById(movieId)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMoviesToCategory should return true when call is success`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movies = mockk<List<MovieEntity>>(relaxed = true)
            val moviesCategory = movies.map { it.toCategoryMovie(category) }
            val expectedResult = true

            coEvery { movieDao.insertMovieList(movies) } just Runs
            coEvery { categoryMovieDao.addMoviesToCategory(moviesCategory) } just Runs

            //When
            val response = roomDataSource.addMoviesToCategory(movies, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMovieToCategory should return true when call is success`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val movieCategory = spyk(CategoryMovieEntity(movieId, category))
            val expectedResult = true

            coEvery { categoryMovieDao.addMovieToCategory(movieCategory) } just Runs

            //When
            val response = roomDataSource.addMovieToCategory(movieId, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `deleteMovieFromCategory should return true when call is success`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val expectedResult = true

            coEvery { categoryMovieDao.deleteMovieFromCategory(movieId, category) } just Runs

            //When
            val response = roomDataSource.deleteMovieFromCategory(movieId, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `deleteMoviesByCategory should return true when call is success`() {
        runTest {
            //Given
            val category = CategoryType.UPCOMING_MOVIES
            val expectedResult = true

            coEvery { movieDao.deleteMoviesByCategory(category) } just Runs

            //When
            val response = roomDataSource.deleteMoviesByCategory(category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMovieToSync should return true when call is success`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val state = SyncState.PENDING_TO_ADD
            val syncCategoryMovie = spyk(SyncCategoryMovieEntity(movieId, category, state))
            val expectedResult = true

            coEvery { syncCategoryMovieDao.addMovieToSync(syncCategoryMovie) } just Runs

            //When
            val response = roomDataSource.addMovieToSync(movieId, category, state)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getMoviesToSync should a list of MovieDetailItem when call is success`() {
        runTest {
            //Given
            val state = SyncState.PENDING_TO_ADD
            val expectedValue = mockk<List<SyncCategoryMovieEntity>>(relaxed = true)
            val expectedResult = expectedValue.map { it.toDomain() }

            coEvery { syncCategoryMovieDao.getMoviesToSync(state) } returns expectedValue

            //When
            val response = roomDataSource.getMoviesToSync(state)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `deleteMovieFromSync should return true when call is success`() {
        runTest {
            //Given
            val movieId = 123456
            val category = CategoryType.HISTORY_MOVIES
            val expectedResult = true

            coEvery { syncCategoryMovieDao.deleteMovieFromSync(movieId, category) } just Runs

            //When
            val response = roomDataSource.deleteMovieFromSync(movieId, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMovieDetailList should return true when call is success`() {
        runTest {
            //Given
            val movie = mockk<MovieDetailEntity>(relaxed = true)
            val expectedResult = true

            coEvery { movieDetailDao.insertMovieDetail(movie) } just Runs

            //When
            val response = roomDataSource.addMovieDetailList(movie)

            //Then
            assertEquals(expectedResult, response)
        }
    }
}