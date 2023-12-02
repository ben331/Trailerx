package com.globant.imdb.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.globant.imdb.data.database.dao.movie.MovieDao
import com.globant.imdb.data.database.dao.movie.CategoryDao
import com.globant.imdb.data.database.dao.movie.CategoryMovieDao
import com.globant.imdb.data.database.entities.movie.MovieEntity
import com.globant.imdb.data.database.entities.movie.CategoryEntity
import com.globant.imdb.data.database.entities.movie.CategoryMovieEntity
import com.globant.imdb.data.database.typeConverters.StringListConverter

@Database(entities = [MovieEntity::class, CategoryEntity::class, CategoryMovieEntity::class], version = 1)
@TypeConverters(StringListConverter::class)
abstract class IMDbDatabase:RoomDatabase() {
    abstract fun getMovieDao():MovieDao
    abstract fun getCategoryDao():CategoryDao
    abstract fun getCategoryMovieDao():CategoryMovieDao
}