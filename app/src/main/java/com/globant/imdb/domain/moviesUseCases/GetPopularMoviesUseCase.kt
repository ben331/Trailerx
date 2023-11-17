package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetPopularMoviesUseCase @Inject constructor( private val repository:IMDbRepository ){
    suspend operator fun invoke():List<MovieItem> {
        val movies = repository.getPopularMoviesFromApi()

        return if ( movies.isNotEmpty() ) {
            val listId = MovieListType.POPULAR_MOVIES.name
            repository.clearMovieList(listId)
            repository.insertPopularMovies( movies.map { it.toDatabase(listId) } )
            movies
        } else {
            repository.getPopularMoviesFromDatabase()
        }
    }
}