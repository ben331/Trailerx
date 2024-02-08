package tech.benhack.movies.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.benhack.movies.datasource.local.room.entities.CategoryEntity

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    suspend fun getAllCategories():List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies:List<CategoryEntity>)

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}