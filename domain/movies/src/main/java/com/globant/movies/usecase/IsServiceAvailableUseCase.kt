package com.globant.movies.usecase

import com.globant.movies.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsServiceAvailableUseCase @Inject constructor(
    repository: MovieRepository,
) {
    val isServiceAvailable: Flow<Boolean> = repository.isServiceAvailable
}