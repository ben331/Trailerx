package com.globant.imdb.di

import com.facebook.CallbackManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FacebookModule {
    @Singleton
    @Provides
    fun provideCallbackManager(): CallbackManager {
        return CallbackManager.Factory.create()
    }
}