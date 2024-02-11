package tech.benhack.movies.usecase

import tech.benhack.movies.repository.MoviesRepository
import javax.inject.Inject

class DeleteUserDataUseCase @Inject constructor(
    private val repository: MoviesRepository,
) {
    suspend operator fun invoke(email: String, authToken:String): Boolean {
        return (
            repository.deleteUserData(email, authToken) &&
            repository.deleteLocalData()
        )
    }
}