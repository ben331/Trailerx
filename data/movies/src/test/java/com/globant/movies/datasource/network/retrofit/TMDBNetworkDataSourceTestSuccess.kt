package com.globant.movies.datasource.network.retrofit

import android.util.Log
import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.movies.mapper.toDomain
import com.globant.movies.model.MovieItem
import com.globant.movies.model.movies.MoviesListDatesModel
import com.globant.movies.model.movies.MoviesListModel
import com.globant.movies.model.movies.VideoListModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.util.Locale
import junit.framework.TestCase.assertEquals

class TMDBNetworkDataSourceTestSuccess {

    @MockK
    lateinit var api : TMDBApiClient

    @InjectMockKs
    lateinit var tmdbNetworkDataSource: TMDBNetworkDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `testService() should return true when call is success`(){
        runTest {
            //Given
            val expectedValue = Response.success((mockk<Void>()))
            val expectedResult = true

            coEvery {
                api.headService()
            } returns expectedValue

            //When
            val response = tmdbNetworkDataSource.testService()

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getNowPlayingMovies() should return a List of MovieItem when calls is success`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val movies = mockk<MoviesListDatesModel>()
            val expectedValue = Response.success(movies)
            val expectedResult = movies.results.map { it.toDomain() }

            coEvery {
                api.getNowPlayingMovies(languageCode, page)
            } returns expectedValue

            //When
            val response = tmdbNetworkDataSource.getNowPlayingMovies()

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getUpcomingMovies() should return a List of MovieItem when calls is success`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val movies = mockk<MoviesListDatesModel>()
            val expectedValue = Response.success(movies)
            val expectedResult = movies.results.map { it.toDomain() }

            coEvery {
                api.getUpcomingMovies(languageCode, page)
            } returns expectedValue

            //When
            val response = tmdbNetworkDataSource.getUpcomingMovies()

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getPopularMovies() should return a List of MovieItem when calls is success`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val movies = mockk<MoviesListModel>()
            val expectedValue = Response.success(movies)
            val expectedResult = movies.results.map { it.toDomain() }

            coEvery {
                api.getPopularMovies(languageCode, page)
            } returns expectedValue

            //When
            val response = tmdbNetworkDataSource.getPopularMovies()

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getMovieById() should return a MovieDetail when calls is success`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val movieId = 123456
            val movie = mockk<MovieDetailModel>()
            val expectedValue = Response.success(movie)
            val expectedResult = movie.toDomain()

            coEvery {
                api.getMovieById(movieId, languageCode)
            } returns expectedValue

            //When
            val response = tmdbNetworkDataSource.getMovieById(movieId)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `searchMovie() should return a List of MovieItem when calls is success`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val query = "query"
            val page = 1
            val movies = mockk<MoviesListModel>()
            val expectedValue = Response.success(movies)
            val expectedResult = movies.results.map { it.toDomain() }

            coEvery {
                api.searchMovie(query, languageCode, page)
            } returns expectedValue

            //When
            val response = tmdbNetworkDataSource.searchMovie(query)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `getTrailers() should return a List of VideoItem when calls is success`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val movieId = 123456
            val trailers = mockk<VideoListModel>()
            val expectedValue = Response.success(trailers)
            val expectedResult = trailers.results.map { it.toDomain() }

            coEvery {
                api.getTrailers(movieId, languageCode)
            } returns expectedValue

            //When
            val response = tmdbNetworkDataSource.getTrailers(movieId)

            //Then
            assertEquals(expectedResult, response)
        }
    }
}