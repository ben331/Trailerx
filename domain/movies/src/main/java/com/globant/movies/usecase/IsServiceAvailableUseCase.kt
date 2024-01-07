package com.globant.movies.usecase

import com.globant.movies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsServiceAvailableUseCase @Inject constructor(
    repository: MoviesRepository,
) {
    val isServiceAvailable: Flow<Boolean> = repository.isServiceAvailable
}