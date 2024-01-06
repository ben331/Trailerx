package com.globant.movies.datasource.local.room.dao

import com.globant.movies.datasource.local.room.entities.CategoryMovieEntity
import com.globant.movies.datasource.local.room.entities.CategoryType

@Dao
interface CategoryMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoviesToCategory(categoryMovies:List<CategoryMovieEntity>)

    @Insert
    suspend fun addMovieToCategory(categoryMovie: CategoryMovieEntity)

    @Query("DELETE FROM category_movie WHERE idMovie=:idMovie AND idCategory=:idCategory")
    suspend fun deleteMovieFromCategory(idMovie:Int, idCategory: CategoryType)
}