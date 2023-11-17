package com.globant.imdb.di

import android.content.Context
import com.globant.imdb.R
import com.globant.imdb.core.Constants
import com.globant.imdb.data.network.retrofit.TMDBApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideAuthInterceptor(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val authToken = context.getString(R.string.TMDB_api_token)
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
            chain.proceed(newRequest)
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client:OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
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