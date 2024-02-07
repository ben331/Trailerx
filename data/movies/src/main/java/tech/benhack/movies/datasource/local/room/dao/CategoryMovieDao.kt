package tech.benhack.movies.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.benhack.common.CategoryType
import tech.benhack.movies.datasource.local.room.entities.CategoryMovieEntity

@Dao
interface CategoryMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoviesToCategory(categoryMovies:List<CategoryMovieEntity>)

    @Insert
    suspend fun addMovieToCategory(categoryMovie: CategoryMovieEntity)

    @Query("DELETE FROM category_movie WHERE idMovie=:idMovie AND idCategory=:idCategory")
    suspend fun deleteMovieFromCategory(idMovie:Int, idCategory: CategoryType)
}