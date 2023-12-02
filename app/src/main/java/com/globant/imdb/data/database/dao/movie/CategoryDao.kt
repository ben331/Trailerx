package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.CategoryEntity
import com.globant.imdb.data.database.entities.movie.CategoryType

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    suspend fun getAllCategories():List<CategoryEntity>

    @Query("SELECT * FROM category WHERE id in (:listTypes)")
    suspend fun getCategoryByType(listTypes:List<CategoryType>):List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies:List<CategoryEntity>)

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}