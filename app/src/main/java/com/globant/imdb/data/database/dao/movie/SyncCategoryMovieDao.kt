package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.SyncCategoryMovieEntity
import com.globant.imdb.data.database.entities.movie.SyncState

@Dao
interface SyncCategoryMovieDao {

    @Query("SELECT * FROM sync_category_movie WHERE sync_category_movie.sync_state = :state")
    suspend fun getMoviesToSync(state: SyncState):List<SyncCategoryMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieToSync(movie:SyncCategoryMovieEntity)

    @Query("DELETE FROM sync_category_movie WHERE idMovie=:idMovie AND idCategory=:idCategory")
    suspend fun deleteMovieFromSync(idMovie:Int, idCategory:CategoryType)
}