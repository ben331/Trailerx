package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.MovieListEntity

@Dao
interface MovieListDao {
    @Query("SELECT * FROM movies_lists")
    suspend fun getAllMoviesLists():List<MovieListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies:List<MovieListEntity>)
}