package com.globant.movies.di

import com.globant.movies.repository.MoviesRepository
import com.globant.movies.repository.MoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMoviesNetworkDataSource(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository
}