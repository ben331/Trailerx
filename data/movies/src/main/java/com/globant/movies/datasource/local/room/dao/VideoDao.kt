package com.globant.movies.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.movies.datasource.local.room.entities.VideoEntity

@Dao
interface VideoDao {
    @Query("SELECT * FROM video WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id:Int): VideoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)
}