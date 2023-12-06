package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import javax.inject.Inject

class TestServiceAvailabilityUseCase @Inject constructor( private val repository: IMDbRepository) {
    suspend operator fun invoke():Boolean = repository.testServiceAvailability()
}
