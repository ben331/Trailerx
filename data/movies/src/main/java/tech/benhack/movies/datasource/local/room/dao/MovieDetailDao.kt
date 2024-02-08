package tech.benhack.movies.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.benhack.movies.datasource.local.room.entities.MovieDetailEntity

@Dao
interface MovieDetailDao {
    @Query("SELECT * FROM movie_detail WHERE id = :id LIMIT 1")
    suspend fun getMovieDetailById(id:Int): MovieDetailEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: MovieDetailEntity)

    @Query("DELETE FROM movie_detail")
    suspend fun clear()
}