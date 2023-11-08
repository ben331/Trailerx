package com.globant.imdb.data.remote.retrofit

import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.data.model.movies.MoviesList
import com.globant.imdb.data.model.movies.MoviesListDates
import com.globant.imdb.data.model.movies.VideoList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.Locale
import javax.inject.Inject

class TMDBService @Inject constructor( private val api: TMDBApiClient ) {
    suspend fun getNowPlayingMovies(): MoviesListDates?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getNowPlayingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getUpcomingMovies(): MoviesListDates?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getUpcomingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getPopularMovies(): MoviesList?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getPopularMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getMovieById(movieId: Int): MovieDetail?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getMovieById( movieId, languageCode )
            response.body()
        }
    }
    suspend fun searchMovie(query: String): MoviesList?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.searchMovie( query, languageCode, 1 )
            response.body()
        }
    }
    suspend fun getTrailers(movieId: Int): VideoList?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = api.getTrailers( movieId, languageCode)
            response.body()
        }
    }
}