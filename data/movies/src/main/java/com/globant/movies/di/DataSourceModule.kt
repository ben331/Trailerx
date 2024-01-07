package com.globant.movies.di

import com.globant.movies.datasource.MoviesLocalDataSource
import com.globant.movies.datasource.MoviesNetworkDataSource
import com.globant.movies.datasource.UserMoviesNetworkDataSource
import com.globant.movies.datasource.local.room.RoomDataSource
import com.globant.movies.datasource.local.room.dao.CategoryDao
import com.globant.movies.datasource.local.room.dao.CategoryMovieDao
import com.globant.movies.datasource.local.room.dao.MovieDao
import com.globant.movies.datasource.local.room.dao.MovieDetailDao
import com.globant.movies.datasource.local.room.dao.SyncCategoryMovieDao
import com.globant.movies.datasource.network.firestore.FirestoreNetworkDataSource
import com.globant.movies.datasource.network.retrofit.TMDBApiClient
import com.globant.movies.datasource.network.retrofit.TMDBNetworkDataSource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {
    @Provides
    fun provideMoviesNetworkDataSource(api:TMDBApiClient): MoviesNetworkDataSource = TMDBNetworkDataSource(api)

    @Provides
    fun provideUserMoviesNetworkDataSource(db: FirebaseFirestore): UserMoviesNetworkDataSource = FirestoreNetworkDataSource(db)

    @Provides
    fun provideMoviesLocalDataSource(
        movieDao: MovieDao,
        categoryDao: CategoryDao,
        categoryMovieDao: CategoryMovieDao,
        syncCategoryMovieDao: SyncCategoryMovieDao,
        movieDetailDao: MovieDetailDao,
        @IoDispatcher ioDispatcher:CoroutineDispatcher
    ): MoviesLocalDataSource = RoomDataSource(
        movieDao,
        categoryDao,
        categoryMovieDao,
        syncCategoryMovieDao,
        movieDetailDao,
        ioDispatcher
    )
}