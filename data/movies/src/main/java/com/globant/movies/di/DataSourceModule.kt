package com.globant.movies.di

import com.globant.movies.datasource.MoviesLocalDataSource
import com.globant.movies.datasource.MoviesNetworkDataSource
import com.globant.movies.datasource.UserMoviesNetworkDataSource
import com.globant.movies.datasource.local.room.RoomDataSource
import com.globant.movies.datasource.network.firestore.FirestoreNetworkDataSource
import com.globant.movies.datasource.network.retrofit.TMDBNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindMoviesNetworkDataSource(tmdbNetworkDataSource: TMDBNetworkDataSource): MoviesNetworkDataSource

    @Binds
    @Singleton
    abstract fun bindUserMoviesNetworkDataSource(firestoreNetworkDataSource: FirestoreNetworkDataSource): UserMoviesNetworkDataSource

    @Binds
    @Singleton
    abstract fun bindMoviesLocalDataSource(roomDataSource: RoomDataSource): MoviesLocalDataSource
}