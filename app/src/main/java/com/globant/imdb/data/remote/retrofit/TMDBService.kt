package com.globant.imdb.data.remote.retrofit

import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.model.movies.MovieDetail
import com.globant.imdb.data.model.movies.MoviesList
import com.globant.imdb.data.model.movies.MoviesListDates
import com.globant.imdb.data.model.movies.VideoList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class TMDBService @Inject constructor() {
    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun getNowPlayingMovies(): MoviesListDates?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getNowPlayingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getUpcomingMovies(): MoviesListDates?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getUpcomingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getPopularMovies(): MoviesList?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getPopularMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getMovieById(movieId: Int): MovieDetail?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getMovieById( movieId, languageCode )
            response.body()
        }
    }
    suspend fun searchMovie(query: String): MoviesList?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).searchMovie( query, languageCode, 1 )
            response.body()
        }
    }
    suspend fun getTrailers(movieId: Int): VideoList?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getTrailers( movieId, languageCode)
            response.body()
        }
    }
}