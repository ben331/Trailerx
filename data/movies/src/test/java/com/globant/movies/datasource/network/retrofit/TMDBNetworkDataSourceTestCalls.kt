package com.globant.movies.datasource.network.retrofit

import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.movies.model.movies.MoviesListDatesModel
import com.globant.movies.model.movies.MoviesListModel
import com.globant.movies.model.movies.VideoListModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.util.Locale

class TMDBNetworkDataSourceTestCalls {

    @MockK
    lateinit var api : TMDBApiClient

    @InjectMockKs
    lateinit var tmdbNetworkDataSource: TMDBNetworkDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `testService() should call headService()`(){
        runTest {
            //Given
            val expectedValue = Response.success((mockk<Void>()))

            coEvery {
                api.headService()
            } returns expectedValue

            //When
            tmdbNetworkDataSource.testService()

            //Then
            coVerify { api.headService() }
        }
    }

    @Test
    fun `getNowPlayingMovies() should call from api getNowPlayingMovies()`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val expectedValue = Response.success(mockk<MoviesListDatesModel>())

            coEvery {
                api.getNowPlayingMovies(languageCode, page)
            } returns expectedValue

            //When
            tmdbNetworkDataSource.getNowPlayingMovies()

            //Then
            coVerify { api.getNowPlayingMovies(languageCode, page) }
        }
    }

    @Test
    fun `getUpcomingMovies() should call from api getUpcomingMovies()`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val expectedValue = Response.success(mockk<MoviesListDatesModel>())

            coEvery {
                api.getUpcomingMovies(languageCode, page)
            } returns expectedValue

            //When
            tmdbNetworkDataSource.getUpcomingMovies()

            //Then
            coVerify { api.getUpcomingMovies(languageCode, page) }
        }
    }

    @Test
    fun `getPopularMovies() should call from api getPopularMovies()`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val expectedValue = Response.success(mockk<MoviesListModel>())

            coEvery {
                api.getPopularMovies(languageCode, page)
            } returns expectedValue

            //When
            tmdbNetworkDataSource.getPopularMovies()

            //Then
            coVerify { api.getPopularMovies(languageCode, page) }
        }
    }

    @Test
    fun `getMovieById() should call from api getMovieById()`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val movieId = 123456
            val expectedValue = Response.success(mockk<MovieDetailModel>())

            coEvery {
                api.getMovieById(movieId, languageCode)
            } returns expectedValue

            //When
            tmdbNetworkDataSource.getMovieById(movieId)

            //Then
            coVerify { api.getMovieById(movieId, languageCode) }
        }
    }

    @Test
    fun `searchMovie() should call from api searchMovie()`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val query = "query"
            val page = 1
            val expectedValue = Response.success(mockk<MoviesListModel>())

            coEvery {
                api.searchMovie(query, languageCode, page)
            } returns expectedValue

            //When
            tmdbNetworkDataSource.searchMovie(query)

            //Then
            coVerify { api.searchMovie(query, languageCode, page) }
        }
    }

    @Test
    fun `getTrailers() should call from api getTrailers()`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val movieId = 123456
            val expectedValue = Response.success(mockk<VideoListModel>())

            coEvery {
                api.getTrailers(movieId, languageCode)
            } returns expectedValue

            //When
            tmdbNetworkDataSource.getTrailers(movieId)

            //Then
            coVerify { api.getTrailers(movieId, languageCode) }
        }
    }
}