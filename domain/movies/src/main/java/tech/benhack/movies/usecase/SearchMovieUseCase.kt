package tech.benhack.movies.usecase

import tech.benhack.movies.model.MovieItem
import tech.benhack.movies.repository.MoviesRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor( private val repository: MoviesRepository) {
    suspend operator fun invoke(query:String):List<MovieItem> = repository.searchMovie(query)
}