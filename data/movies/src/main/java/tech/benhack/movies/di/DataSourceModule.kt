package tech.benhack.movies.di

import tech.benhack.di.DispatcherModule
import tech.benhack.movies.datasource.MoviesLocalDataSource
import tech.benhack.movies.datasource.MoviesNetworkDataSource
import tech.benhack.movies.datasource.UserMoviesNetworkDataSource
import tech.benhack.movies.datasource.local.room.RoomDataSource
import tech.benhack.movies.datasource.network.firestore.FirestoreNetworkDataSource
import tech.benhack.movies.datasource.network.retrofit.TMDBNetworkDataSource
import dagger.Binds
import dagger.Module
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