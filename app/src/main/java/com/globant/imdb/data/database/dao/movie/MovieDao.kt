package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.globant.imdb.data.database.entities.movie.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id:Int):MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movieList:List<MovieEntity>)

    @Update
    suspend fun updateMovie(movie:MovieEntity)

    @Query("SELECT movie.* FROM movie INNER JOIN category_movie ON movie.id = category_movie.idMovie WHERE category_movie.idCategory = :categoryId")
    suspend fun getMoviesByCategory(categoryId:String):List<MovieEntity>

    @Query("DELETE FROM movie WHERE id IN (SELECT idMovie FROM category_movie WHERE idCategory = :categoryId)")
    suspend fun deleteMoviesByCategory(categoryId:String)
}