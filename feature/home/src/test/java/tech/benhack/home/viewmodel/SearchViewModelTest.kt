package tech.benhack.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.usecase.IsServiceAvailableUseCase
import tech.benhack.movies.usecase.SearchMovieUseCase
import tech.benhack.ui.helpers.NetworkState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    var dispatcher = UnconfinedTestDispatcher()

    @MockK
    lateinit var searchMovieUseCase: SearchMovieUseCase
    @MockK
    lateinit var isServiceAvailableUseCase: IsServiceAvailableUseCase

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup(){
        MockKAnnotations.init(this)

        val fakeFlow = flow { delay(1000); emit(false) }
        coEvery { isServiceAvailableUseCase.isServiceAvailable } returns fakeFlow

        searchViewModel = SearchViewModel(
            searchMovieUseCase,
            isServiceAvailableUseCase,
            dispatcher
        )
    }

    @Test
    fun `search() should call search() from useCase`(){
        runTest {
            //Given
            val query = "query"
            val expectedMovie = mockk<MovieItem>(relaxed = true)
            val expectedResult = listOf(expectedMovie)

            coEvery { searchMovieUseCase(query) } returns expectedResult

            //When
            searchViewModel.search(query)

            //Then
            coVerify { searchMovieUseCase(query)  }
            assertEquals(expectedResult, searchViewModel.resultMovies.value)
        }
    }

    @Test
    fun `uiState should be Loading when viewModel just initialized`(){
        val fakeFlow = flow { delay(1000); emit(false) }
        coEvery { isServiceAvailableUseCase.isServiceAvailable } returns fakeFlow

        searchViewModel = SearchViewModel(
            searchMovieUseCase,
            isServiceAvailableUseCase,
            dispatcher
        )

        runTest {
            assertEquals(NetworkState.Loading, searchViewModel.uiState.value)
        }
    }

    @Test
    fun `uiState should be Offline When flow value is false`(){
        val fakeFlow = flow { emit(false) }
        coEvery { isServiceAvailableUseCase.isServiceAvailable } returns fakeFlow

        searchViewModel = SearchViewModel(
            searchMovieUseCase,
            isServiceAvailableUseCase,
            dispatcher
        )

        runTest {
            assertEquals(NetworkState.Offline, searchViewModel.uiState.value)
        }
    }

    @Test
    fun `uiState should be Online When flow value is true`(){
        val fakeFlow = flow { emit(true) }
        coEvery { isServiceAvailableUseCase.isServiceAvailable } returns fakeFlow

        searchViewModel = SearchViewModel(
            searchMovieUseCase,
            isServiceAvailableUseCase,
            dispatcher
        )

        runTest {
            assertEquals(NetworkState.Online, searchViewModel.uiState.value)
        }
    }
}