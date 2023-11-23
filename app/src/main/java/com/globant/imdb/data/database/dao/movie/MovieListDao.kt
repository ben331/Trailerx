package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.MovieListEntity
import com.globant.imdb.data.database.entities.movie.MovieListType

@Dao
interface MovieListDao {
    @Query("SELECT * FROM movies_lists")
    suspend fun getAllMoviesLists():List<MovieListEntity>

    @Query("SELECT * FROM movies_lists WHERE id in (:listTypes)")
    suspend fun getMoviesListsByTypes(listTypes:List<MovieListType>):List<MovieListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies:List<MovieListEntity>)

    @Query("DELETE FROM movies_lists")
    suspend fun deleteAll()
}