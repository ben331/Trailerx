package com.globant.movies.datasource.local.room.dao

import com.globant.movies.datasource.local.room.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    suspend fun getAllCategories():List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies:List<CategoryEntity>)

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}