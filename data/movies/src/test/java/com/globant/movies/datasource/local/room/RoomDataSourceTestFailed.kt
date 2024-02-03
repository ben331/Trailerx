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
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.SyncCategoryMovieItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoomDataSourceTestFailed {
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
    fun `getMoviesByCategory should return null when first call is error`() {
        runTest {
            //Given
            val category = CategoryType.POPULAR_MOVIES
            val expectedResult:List<MovieItem>? = null

            coEvery { movieDao.getMoviesByCategory(category) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.getMoviesByCategory(category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getMovieById should return return null when second call is error`() {
        runTest {
            //Given
            val movieId = 123456
            val expectedResult:MovieDetailItem? = null

            coEvery { movieDetailDao.getMovieDetailById(movieId) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.getMovieById(movieId)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getMovieById should return null when call is error`() {
        runTest {
            //Given
            val movieId = 123456
            val expectedMovieDetail:MovieDetailEntity? = null
            val expectedResult:MovieDetailItem? = null

            coEvery { movieDetailDao.getMovieDetailById(movieId) } returns expectedMovieDetail
            coEvery { movieDao.getMovieById(movieId) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.getMovieById(movieId)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMoviesToCategory should return false when call is error`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movies = mockk<List<MovieEntity>>(relaxed = true)
            val expectedResult = false

            coEvery { movieDao.insertMovieList(movies) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.addMoviesToCategory(movies, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMovieToCategory should return false when call is error`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val movieCategory = spyk(CategoryMovieEntity(movieId, category))
            val expectedResult = false

            coEvery { categoryMovieDao.addMovieToCategory(movieCategory) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.addMovieToCategory(movieId, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `deleteMovieFromCategory should return false when call is error`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val expectedResult = false

            coEvery { categoryMovieDao.deleteMovieFromCategory(movieId, category) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.deleteMovieFromCategory(movieId, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `deleteMoviesByCategory should return false when call is error`() {
        runTest {
            //Given
            val category = CategoryType.UPCOMING_MOVIES
            val expectedResult = false

            coEvery { movieDao.deleteMoviesByCategory(category) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.deleteMoviesByCategory(category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMovieToSync should return false when call is error`() {
        runTest {
            //Given
            val category = CategoryType.HISTORY_MOVIES
            val movieId = 123456
            val state = SyncState.PENDING_TO_ADD
            val syncCategoryMovie = spyk(SyncCategoryMovieEntity(movieId, category, state))
            val expectedResult = false

            coEvery { syncCategoryMovieDao.addMovieToSync(syncCategoryMovie) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.addMovieToSync(movieId, category, state)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getMoviesToSync should return null when call is error`() {
        runTest {
            //Given
            val state = SyncState.PENDING_TO_ADD
            val expectedResult:List<SyncCategoryMovieItem>? = null

            coEvery { syncCategoryMovieDao.getMoviesToSync(state) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.getMoviesToSync(state)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `deleteMovieFromSync should return false when call is error`() {
        runTest {
            //Given
            val movieId = 123456
            val category = CategoryType.HISTORY_MOVIES
            val expectedResult = false

            coEvery { syncCategoryMovieDao.deleteMovieFromSync(movieId, category) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.deleteMovieFromSync(movieId, category)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMovieDetailList should return false when call is error`() {
        runTest {
            //Given
            val movie = mockk<MovieDetailEntity>(relaxed = true)
            val expectedResult = false

            coEvery { movieDetailDao.insertMovieDetail(movie) } throws mockk<Exception>(relaxed = true)

            //When
            val response = roomDataSource.addMovieDetailList(movie)

            //Then
            assertEquals(expectedResult, response)
        }
    }
}