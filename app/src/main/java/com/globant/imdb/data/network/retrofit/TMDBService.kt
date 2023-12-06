package com.globant.imdb.data.network.retrofit

import com.globant.imdb.core.Constants
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
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getNowPlayingMovies( languageCode,1 )
                response.body()
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun getUpcomingMovies(): MoviesListDatesModel?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getUpcomingMovies( languageCode,1 )
                response.body()
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun getPopularMovies(): MoviesListModel?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getPopularMovies( languageCode,1 )
                response.body()
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun getMovieById(movieId: Int): MovieDetailModel?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getMovieById( movieId, languageCode )
                response.body()
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun searchMovie(query: String): MoviesListModel?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.searchMovie( query, languageCode, 1 )
                response.body()
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }
    suspend fun getTrailers(movieId: Int): VideoListModel?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getTrailers( movieId, languageCode)
                response.body()
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun testServiceAvailability(): Boolean {
        return withContext(Dispatchers.IO){
            try {
                val response = api.testServiceAvailability()
                response.body() != null
            }catch (e: Exception){
                false
            }
        }
    }
}