package com.globant.movies.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.globant.movies.dao.MovieDao
import com.globant.movies.datasource.local.room.dao.CategoryDao
import com.globant.movies.datasource.local.room.dao.CategoryMovieDao
import com.globant.movies.datasource.local.room.dao.MovieDetailDao
import com.globant.movies.datasource.local.room.dao.SyncCategoryMovieDao
import com.globant.movies.datasource.local.room.entities.MovieEntity
import com.globant.movies.datasource.local.room.entities.CategoryEntity
import com.globant.movies.datasource.local.room.entities.CategoryMovieEntity
import com.globant.movies.datasource.local.room.entities.MovieDetailEntity
import com.globant.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import com.globant.movies.datasource.local.room.typeconverter.StringListConverter

@Database(entities = [
    MovieEntity::class,
    CategoryEntity::class,
    CategoryMovieEntity::class,
    SyncCategoryMovieEntity::class,
    MovieDetailEntity::class
 ], version = 1)
@TypeConverters(StringListConverter::class)
abstract class IMDbDatabase:RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getCategoryMovieDao(): CategoryMovieDao
    abstract fun getSyncCategoryMovieDao(): SyncCategoryMovieDao
    abstract fun getMovieDetailDao(): MovieDetailDao
}