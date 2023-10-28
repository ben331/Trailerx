package com.globant.imdb.data.remote.retrofit

import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.model.MoviesList
import com.globant.imdb.data.model.MoviesListDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

class TMDBService {
    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun getNowPlayingMovies():MoviesListDates?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getNowPlayingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getUpcomingMovies():MoviesListDates?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getUpcomingMovies( languageCode,1 )
            response.body()
        }
    }
    suspend fun getPopularMovies():MoviesList?{
        return withContext(Dispatchers.IO){
            val languageCode = Locale.getDefault().language
            val response = retrofit
                .create(TMDBApiClient::class.java).getPopularMovies( languageCode,1 )
            response.body()
        }
    }
}