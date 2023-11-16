package com.globant.imdb.data.remote.retrofit

import com.globant.imdb.data.model.movies.MovieDetailModel
import com.globant.imdb.data.model.movies.MoviesListModel
import com.globant.imdb.data.model.movies.MoviesListDatesModel
import com.globant.imdb.data.model.movies.VideoListModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class TMDBService @Inject constructor( private val api:TMDBApiClient ) {
    suspend fun getNowPlayingMovies(): MoviesListDatesModel?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getNowPlayingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getUpcomingMovies(): MoviesListDatesModel?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getUpcomingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getPopularMovies(): MoviesListModel?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getPopularMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getMovieById(movieId: Int): MovieDetailModel?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getMovieById( movieId, languageCode )
            response.body()
        }
    }
    suspend fun searchMovie(query: String): MoviesListModel?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.searchMovie( query, languageCode, 1 )
            response.body()
        }
    }
    suspend fun getTrailers(movieId: Int): VideoListModel?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getTrailers( movieId, languageCode)
            response.body()
        }
    }
}