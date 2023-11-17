package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.database.entities.movie.MovieListType
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke():List<MovieItem> {
        val movies = repository.getNowPlayingMoviesFromApi()

        return if ( movies.isNotEmpty() ) {
            val listId = MovieListType.NOW_PLAYING_MOVIES.name
            repository.clearMovieList(listId)
            repository.insertNowPlayingMovies( movies.map { it.toDatabase(listId) } )
            movies
        } else {
            repository.getNowPlayingMoviesFromDatabase()
        }
    }
}