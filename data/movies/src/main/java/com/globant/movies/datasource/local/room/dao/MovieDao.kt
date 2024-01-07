package com.globant.movies.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.common.CategoryType
import com.globant.movies.datasource.local.room.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

private val homeMovies = listOf(
    CategoryType.NOW_PLAYING_MOVIES,
    CategoryType.WATCH_LIST_MOVIES,
    CategoryType.POPULAR_MOVIES
)

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id:Int):MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movieList:List<MovieEntity>)

    @Query("SELECT movie.* FROM movie INNER JOIN category_movie ON movie.id = category_movie.idMovie WHERE category_movie.idCategory = :categoryId")
    suspend fun getMoviesByCategory(categoryId:CategoryType):List<MovieEntity>

    @Query("SELECT * FROM movie")
    fun getMoviesFlow(): Flow<List<MovieEntity>>

    @Query("DELETE FROM movie WHERE id IN (SELECT idMovie FROM category_movie WHERE idCategory = :categoryId)")
    suspend fun deleteMoviesByCategory(categoryId:CategoryType)
}
