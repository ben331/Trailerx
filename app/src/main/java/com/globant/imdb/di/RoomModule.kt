package com.globant.imdb.di

import android.content.Context
import androidx.room.Room
import com.globant.imdb.data.database.IMDbDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val IMDb_DATABASE_NAME = "imdb_database"

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context:Context):IMDbDatabase =
        Room.databaseBuilder(context, IMDbDatabase::class.java, IMDb_DATABASE_NAME).build()

    @Provides
    @Singleton
    fun provideMovieDao(db:IMDbDatabase) = db.getMovieDao()

    @Provides
    @Singleton
    fun provideCategoryDao(db:IMDbDatabase) = db.getCategoryDao()

    @Provides
    @Singleton
    fun provideCategoryMovieDao(db:IMDbDatabase) = db.getCategoryMovieDao()
}