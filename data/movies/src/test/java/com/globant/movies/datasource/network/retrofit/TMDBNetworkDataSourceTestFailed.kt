package com.globant.movies.datasource.network.retrofit

import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.VideoItem
import junit.framework.TestCase.assertEquals
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

class TMDBNetworkDataSourceTestFailed {

    @MockK
    lateinit var api : TMDBApiClient

    @InjectMockKs
    lateinit var tmdbNetworkDataSource: TMDBNetworkDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `testService() should return false when call is error`(){
        runTest {
            //Given
            val codeError = 500
            val expectedValue: Response<Void> = Response.error(codeError, mockk(relaxed = true))
            val expectedResult = false

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
    fun `getNowPlayingMovies() should return null when calls is error`(){
        //Exception case
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val expectedResult:List<MovieItem>? = null

            coEvery {
                api.getNowPlayingMovies(languageCode, page)
            } throws mockk<Exception>()

            //When
            val response = tmdbNetworkDataSource.getNowPlayingMovies()

            //Then
            assertEquals(expectedResult, response)
        }

        //Server error case
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val codeError = 500
            val expectedValue: Response<MoviesListDatesModel?> = Response.error(codeError, mockk(relaxed = true))
            val expectedResult:List<MovieItem>? = null

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
    fun `getUpcomingMovies() should return null when calls is error`(){
        //Exception case
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val expectedResult:List<MovieItem>? = null

            coEvery {
                api.getUpcomingMovies(languageCode, page)
            } throws mockk<Exception>()

            //When
            val response = tmdbNetworkDataSource.getUpcomingMovies()

            //Then
            assertEquals(expectedResult, response)
        }

        //Server error case
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val codeError = 500
            val expectedValue: Response<MoviesListDatesModel?> = Response.error(codeError, mockk(relaxed = true))
            val expectedResult:List<MovieItem>? = null

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
    fun `getPopularMovies() should return null when calls is error`(){
        //Exception case
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val expectedResult:List<MovieItem>? = null

            coEvery {
                api.getPopularMovies(languageCode, page)
            } throws mockk<Exception>()

            //When
            val response = tmdbNetworkDataSource.getPopularMovies()

            //Then
            assertEquals(expectedResult, response)
        }

        //Server error case
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val page = 1
            val codeError = 500
            val expectedValue: Response<MoviesListModel?> = Response.error(codeError, mockk(relaxed = true))
            val expectedResult:List<MovieItem>? = null

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
    fun `getMovieById() should return null when calls is error`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val movieId = 123456
            val expectedResult: MovieDetailItem? = null

            coEvery {
                api.getMovieById(movieId, languageCode)
            } throws mockk<Exception>()

            //When
            val response = tmdbNetworkDataSource.getMovieById(movieId)

            //Then
            assertEquals(expectedResult, response)
        }

        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val movieId = 123456
            val codeError = 500
            val expectedValue:Response<MovieDetailModel?> = Response.error(codeError, mockk(relaxed = true))
            val expectedResult: MovieDetailItem? = null

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
    fun `searchMovie() should return null when calls is error`(){
        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val query = "query"
            val page = 1
            val expectedResult:List<MovieItem>? = null

            coEvery {
                api.searchMovie(query, languageCode, page)
            } throws mockk<Exception>()

            //When
            val response = tmdbNetworkDataSource.searchMovie(query)

            //Then
            assertEquals(expectedResult, response)
        }

        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val query = "query"
            val page = 1
            val codeError = 500
            val expectedValue:Response<MoviesListModel?> = Response.error(codeError, mockk(relaxed = true))
            val expectedResult:List<MovieItem>? = null

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
            val expectedResult:List<VideoItem>? = null

            coEvery {
                api.getTrailers(movieId, languageCode)
            } throws mockk<Exception>()

            //When
            val response = tmdbNetworkDataSource.getTrailers(movieId)

            //Then
            assertEquals(expectedResult, response)
        }

        runTest {
            //Given
            val languageCode = Locale.getDefault().language
            val movieId = 123456
            val codeError = 500
            val expectedValue:Response<VideoListModel?> = Response.error(codeError, mockk(relaxed = true))
            val expectedResult:List<VideoItem>? = null

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