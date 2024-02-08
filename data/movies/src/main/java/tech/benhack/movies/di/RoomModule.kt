package tech.benhack.movies.di

import android.content.Context
import androidx.room.Room
import tech.benhack.movies.datasource.local.room.TrailerxDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val Trailerx_DATABASE_NAME = "trailerx_database"

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context:Context):TrailerxDatabase =
        Room.databaseBuilder(context, TrailerxDatabase::class.java, Trailerx_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideMovieDao(db:TrailerxDatabase) = db.getMovieDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db:TrailerxDatabase) = db.getCategoryDao()

    @Provides
    @Singleton
    fun provideCategoryMovieDao(db:TrailerxDatabase) = db.getCategoryMovieDao()

    @Provides
    @Singleton
    fun provideSyncCategoryMovieDao(db:TrailerxDatabase) = db.getSyncCategoryMovieDao()

    @Provides
    @Singleton
    fun provideMovieDetailDao(db:TrailerxDatabase) = db.getMovieDetailDao()
}