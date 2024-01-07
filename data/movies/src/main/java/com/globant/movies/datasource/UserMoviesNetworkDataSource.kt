package com.globant.movies.datasource

import com.globant.common.CategoryType
import com.globant.movies.model.MovieItem

interface UserMoviesNetworkDataSource {
    suspend fun getUserMoviesList(listType: CategoryType, email:String):List<MovieItem>?
    suspend fun addMovieToList(movie: MovieItem, category: CategoryType, email:String):Boolean
    suspend fun deleteMovieFromList(movieId:Int, category: CategoryType, email:String):Boolean
}