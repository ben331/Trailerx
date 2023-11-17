package com.globant.imdb.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globant.imdb.data.database.dao.movie.MovieDao
import com.globant.imdb.data.database.dao.movie.MovieListDao
import com.globant.imdb.data.database.entities.movie.MovieEntity
import com.globant.imdb.data.database.entities.movie.MovieListEntity

@Database(entities = [MovieEntity::class, MovieListEntity::class], version = 1)
abstract class IMDbDatabase:RoomDatabase() {
    abstract fun getMovieDao():MovieDao
    abstract fun getMovieListDao():MovieListDao
}