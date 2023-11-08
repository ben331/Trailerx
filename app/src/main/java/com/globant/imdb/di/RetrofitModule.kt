package com.globant.imdb.di

import com.globant.imdb.core.Constants
import com.globant.imdb.data.remote.retrofit.TMDBApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileReader
import java.util.Properties
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${getTMDBApiKey()}")
                .build()
            chain.proceed(newRequest)
        }.build()

        return Retrofit.Builder()
            .baseUrl(Constants.TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideTMDBApiClient(retrofit: Retrofit): TMDBApiClient {
        return retrofit.create(TMDBApiClient::class.java)
    }

    private fun getTMDBApiKey():String{
        val properties = Properties()
        val localPropertiesPath = "local.properties"
        try {
            properties.load(FileReader(localPropertiesPath))
            return properties.getProperty("tmdb.api.key")
        }catch (e:Exception){
            e.printStackTrace()
        }
        return ""
    }
}