package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val CLOSE_FLOW_DELAY = 5000L

class IsNetworkAvailableUseCase @Inject constructor(
    private val repository: IMDbRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.isConnectionAvailable
}