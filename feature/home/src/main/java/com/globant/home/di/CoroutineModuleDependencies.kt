package com.globant.home.di

import com.globant.di.DefaultDispatcher
import com.globant.di.IoDispatcher
import com.globant.di.MainDispatcher
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CoroutineModuleDependencies {
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher

    @IoDispatcher
    fun providesIoDispatcher(): CoroutineDispatcher

    @MainDispatcher
    fun providesMainDispatcher(): CoroutineDispatcher
}