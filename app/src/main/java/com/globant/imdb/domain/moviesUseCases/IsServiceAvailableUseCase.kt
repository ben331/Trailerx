package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsServiceAvailableUseCase @Inject constructor(
    repository: IMDbRepository,
) {
    val isServiceAvailable: Flow<Boolean> = repository.isServiceAvailable
}