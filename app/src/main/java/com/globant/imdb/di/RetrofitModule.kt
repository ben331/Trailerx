package com.globant.imdb.di

import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.remote.retrofit.TMDBApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit{
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${RetrofitHelper.authToken}")
                .build()
            chain.proceed(newRequest)
        }.build()

        return Retrofit.Builder()
            .baseUrl(RetrofitHelper.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideTMDBApiClient(retrofit: Retrofit):TMDBApiClient{
        return retrofit.create(TMDBApiClient::class.java)
    }
}