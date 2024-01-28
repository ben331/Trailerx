package com.globant.movies.datasource.network.retrofit

import com.globant.movies.datasource.MoviesNetworkDataSource
import com.globant.movies.mapper.toDomain
import com.globant.movies.model.MovieDetailItem
import com.globant.movies.model.MovieItem
import com.globant.movies.model.VideoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class TMDBNetworkDataSource @Inject constructor(private val api:TMDBApiClient ): MoviesNetworkDataSource {

    override suspend fun testService():Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.headService()
                response.code() in 200..299
            } catch (_:Exception) {
                false
            }
        }
    }

    override suspend fun getNowPlayingMovies(): List<MovieItem>?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getNowPlayingMovies( languageCode,1 )
                if( response.code() in 200..299){
                    response.body()?.results?.map { it.toDomain() }
                }else{
                    null
                }
            }catch (e: Exception){
                null
            }
        }
    }
    override suspend fun getUpcomingMovies(): List<MovieItem>?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getUpcomingMovies( languageCode,1 )
                if( response.code() in 200..299){
                    response.body()?.results?.map { it.toDomain() }
                }else{
                    null
                }
            }catch (e: Exception){
                null
            }
        }
    }
    override suspend fun getPopularMovies(): List<MovieItem>?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getPopularMovies( languageCode,1 )
                if( response.code() in 200..299){
                    response.body()?.results?.map { it.toDomain() }
                }else{
                    null
                }
            }catch (e: Exception){
                null
            }
        }
    }
    override suspend fun getMovieById(movieId: Int): MovieDetailItem?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getMovieById( movieId, languageCode )
                if( response.code() in 200..299){
                    response.body()?.toDomain()
                }else{
                    null
                }
            }catch (e: Exception){
                null
            }
        }
    }
    override suspend fun searchMovie(query: String): List<MovieItem>? {
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.searchMovie( query, languageCode, 1 )
                if( response.code() in 200..299){
                    response.body()?.results?.map { it.toDomain() }
                }else{
                    null
                }
            }catch (e: Exception){
                null
            }
        }
    }
    override suspend fun getTrailers(movieId: Int): List<VideoItem>?{
        return withContext(Dispatchers.IO){
            try {
                val languageCode = Locale.getDefault().language
                val response = api.getTrailers( movieId, languageCode)
                if( response.code() in 200..299){
                    response.body()?.results?.map { it.toDomain() }
                }else{
                    null
                }
            }catch (e: Exception){
                null
            }
        }
    }
}