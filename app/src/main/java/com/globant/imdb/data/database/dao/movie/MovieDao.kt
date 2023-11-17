package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.MovieEntity

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE listId = :listId")
    suspend fun getListOfMovies(listId:Int):List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movieList:List<MovieEntity>)
}