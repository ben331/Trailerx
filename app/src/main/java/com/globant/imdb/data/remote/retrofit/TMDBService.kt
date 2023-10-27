package com.globant.imdb.data.remote.retrofit

import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.model.Movie
import com.globant.imdb.data.model.MovieList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TMDBService {
    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun getTopRatedMovies():MovieList?{
        return withContext(Dispatchers.IO){
            val response = retrofit
                .create(TMDBApiClient::class.java).getTopRatedMovies("en-US",1)
            response.body()
        }
    }
}