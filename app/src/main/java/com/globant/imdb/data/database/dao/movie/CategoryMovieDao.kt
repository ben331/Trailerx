package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.CategoryMovieEntity
import com.globant.imdb.data.database.entities.movie.MovieEntity

@Dao
interface CategoryMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoviesToCategory(categoryMovies:List<CategoryMovieEntity>)
}