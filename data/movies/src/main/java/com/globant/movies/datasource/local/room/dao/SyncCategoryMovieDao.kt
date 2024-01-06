package com.globant.movies.datasource.local.room.dao

import com.globant.movies.datasource.local.room.entities.CategoryType
import com.globant.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import com.globant.movies.datasource.local.room.entities.SyncState

@Dao
interface SyncCategoryMovieDao {

    @Query("SELECT * FROM sync_category_movie WHERE sync_state = :state")
    suspend fun getMoviesToSync(state: SyncState):List<SyncCategoryMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToSync(movie: SyncCategoryMovieEntity)

    @Query("DELETE FROM sync_category_movie WHERE idMovie=:idMovie AND idCategory=:idCategory")
    suspend fun deleteMovieFromSync(idMovie:Int, idCategory: CategoryType)
}