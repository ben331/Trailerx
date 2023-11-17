package com.globant.imdb.data.database.dao.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globant.imdb.data.database.entities.movie.MovieListEntity
import com.globant.imdb.data.database.entities.movie.VideoEntity

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id:Int):VideoEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video:VideoEntity)
}