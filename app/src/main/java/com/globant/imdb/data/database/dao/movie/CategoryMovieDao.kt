package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.globant.imdb.data.database.entities.movie.CategoryMovieEntity

@Dao
interface CategoryMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToCategory(categoryMovies:List<CategoryMovieEntity>)
}