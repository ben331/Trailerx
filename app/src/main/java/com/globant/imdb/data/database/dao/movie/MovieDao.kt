package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE listId = :listId")
    suspend fun getListOfMovies(listId:String):List<MovieEntity>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getMovieById(id:Int):MovieEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movieList:List<MovieEntity>)

    @Query("DELETE FROM movies WHERE listId = :listId")
    suspend fun deleteMovieList(listId:String)

    @Query("UPDATE movies SET tagline = :tagline WHERE id = :id")
    suspend fun updateTagLine(id:Int, tagline:String?)
}