package com.globant.imdb.di

import com.globant.auth.di.FeatureAuthModule
import com.globant.home.di.FeatureHomeModule
import com.globant.movies.di.MoviesDomainModule
import com.globant.movies.di.MoviesRepositoryModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        MoviesRepositoryModule::class,
        MoviesDomainModule::class,
        FeatureAuthModule::class,
        FeatureHomeModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule