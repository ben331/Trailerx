package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.repositories.IMDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class IsNetworkAvailableUseCase @Inject constructor(
    repository: IMDbRepository,
) {
    val isConnectionAvailable: Flow<Boolean> = repository.isConnectionAvailable.catch { emit(true) }
}