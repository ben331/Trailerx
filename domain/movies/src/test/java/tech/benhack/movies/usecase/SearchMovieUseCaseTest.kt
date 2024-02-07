package tech.benhack.movies.usecase

import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchMovieUseCaseTest {
    @MockK
    lateinit var repository: MoviesRepository

    @InjectMockKs
    lateinit var searchMovieUseCase: SearchMovieUseCase

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `searchMovieUseCase() should call searchMovie() from repository`(){
        runTest {
            //Given
            val query = "query"
            val expectedValue = mockk<List<MovieItem>>()

            coEvery { repository.searchMovie(query) } returns expectedValue

            //When
            repository.searchMovie(query)

            //Then
            coVerify { repository.searchMovie(query) }
        }
    }
}