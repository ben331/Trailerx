package tech.benhack.movies.usecase

import tech.benhack.movies.model.MovieDetailItem
import tech.benhack.movies.repository.MoviesRepository
import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor( private val repository: MoviesRepository) {
    suspend operator fun invoke(movieId:Int):MovieDetailItem? {
        val movie = repository.getMovieByIdFromLocal(movieId)

        if(movie == null || movie.tagline.isNullOrEmpty()) {
            val movieUpdated = repository.getMovieByIdFromApi(movieId)
            if(movieUpdated != null){
                repository.addMovieDetailLocal(movieUpdated)
                return movieUpdated
            }
        }
        return movie
    }
}