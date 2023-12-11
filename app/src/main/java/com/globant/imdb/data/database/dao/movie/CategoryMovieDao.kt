package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.CategoryMovieEntity
import com.globant.imdb.data.database.entities.movie.CategoryType

@Dao
interface CategoryMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoviesToCategory(categoryMovies:List<CategoryMovieEntity>)

    @Insert
    suspend fun addMovieToCategory(categoryMovie:CategoryMovieEntity)

    @Query("DELETE FROM category_movie WHERE idMovie=:idMovie AND idCategory=:idCategory")
    suspend fun deleteMovieFromCategory(idMovie:Int, idCategory:CategoryType)
}