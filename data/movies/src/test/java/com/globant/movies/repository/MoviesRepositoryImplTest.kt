package com.globant.movies.repository

import com.globant.movies.datasource.MoviesLocalDataSource
import com.globant.movies.datasource.MoviesNetworkDataSource
import com.globant.movies.datasource.UserMoviesNetworkDataSource
import com.globant.movies.model.MovieItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImplTest {
    @MockK
    lateinit var moviesNetworkDataSource: MoviesNetworkDataSource
    @MockK
    lateinit var userMoviesNetworkDataSource: UserMoviesNetworkDataSource
    @MockK
    lateinit var moviesLocalDataSource: MoviesLocalDataSource

    @InjectMockKs
    lateinit var moviesRepositoryImpl: MoviesRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `isServiceAvailable should call flow from moviesNetworkDataSource`(){
        runTest {
            //Given
            coEvery { moviesNetworkDataSource.testService() } returns true

            //When
            val response = moviesRepositoryImpl.isServiceAvailable.first()

            //Then
            coVerify { moviesNetworkDataSource.testService() }
            assertEquals(true, response)
        }
    }

    @Test
    fun `searchMovie should call searchMovie from moviesNetworkDataSource`(){
        runTest {
            //Given
            val query = "query"
            val expectedValue = mockk<List<MovieItem>>(relaxed = true)

            coEvery { moviesNetworkDataSource.searchMovie(query) } returns expectedValue

            //When
            val response = moviesRepositoryImpl.searchMovie(query)

            //Then
            coVerify { moviesNetworkDataSource.searchMovie(query) }
            assertEquals(expectedValue, response)
        }
    }
}