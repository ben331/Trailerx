package tech.benhack.movies.datasource.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tech.benhack.movies.datasource.local.room.dao.CategoryDao
import tech.benhack.movies.datasource.local.room.dao.CategoryMovieDao
import tech.benhack.movies.datasource.local.room.dao.MovieDao
import tech.benhack.movies.datasource.local.room.dao.MovieDetailDao
import tech.benhack.movies.datasource.local.room.dao.SyncCategoryMovieDao
import tech.benhack.movies.datasource.local.room.entities.MovieEntity
import tech.benhack.movies.datasource.local.room.entities.CategoryEntity
import tech.benhack.movies.datasource.local.room.entities.CategoryMovieEntity
import tech.benhack.movies.datasource.local.room.entities.MovieDetailEntity
import tech.benhack.movies.datasource.local.room.entities.SyncCategoryMovieEntity
import tech.benhack.movies.datasource.local.room.typeconverter.StringListConverter

@Database(entities = [
    MovieEntity::class,
    CategoryEntity::class,
    CategoryMovieEntity::class,
    SyncCategoryMovieEntity::class,
    MovieDetailEntity::class
 ], version = 1)
@TypeConverters(StringListConverter::class)
abstract class TrailerxDatabase:RoomDatabase() {
    abstract fun getMovieDao(): MovieDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getCategoryMovieDao(): CategoryMovieDao
    abstract fun getSyncCategoryMovieDao(): SyncCategoryMovieDao
    abstract fun getMovieDetailDao(): MovieDetailDao
}