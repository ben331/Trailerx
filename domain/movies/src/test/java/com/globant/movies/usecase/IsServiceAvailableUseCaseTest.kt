package com.globant.movies.usecase

import com.globant.movies.repository.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IsServiceAvailableUseCaseTest {
    @MockK
    lateinit var moviesRepository: MoviesRepository

    private lateinit var isServiceAvailableUseCase: IsServiceAvailableUseCase

    @Before
    fun setup(){
        MockKAnnotations.init(this)

        val fakeFlow = flow { emit(true) }
        coEvery { moviesRepository.isServiceAvailable } returns fakeFlow
        isServiceAvailableUseCase = IsServiceAvailableUseCase(moviesRepository)

    }

    @Test
    fun `isServiceAvailableUseCase should call isServiceAvailable() from repository`(){
        runTest { isServiceAvailableUseCase
            //When
            val response = isServiceAvailableUseCase.isServiceAvailable.first()

            //Then
            coVerify { moviesRepository.isServiceAvailable }
            assertEquals(true, response)
        }
    }
}