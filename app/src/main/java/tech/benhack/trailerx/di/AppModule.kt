package tech.benhack.trailerx.di

import tech.benhack.auth.di.FeatureAuthModule
import tech.benhack.home.di.FeatureHomeModule
import tech.benhack.movies.di.MoviesDomainModule
import tech.benhack.movies.di.MoviesRepositoryModule
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