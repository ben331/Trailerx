package com.globant.imdb.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import com.globant.imdb.R

object RetrofitHelper {
    private val authToken = R.string.tmdb_api_token.toString()
    private val authInterceptor = AuthInterceptor(authToken)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

class AuthInterceptor(private val authToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $authToken")
            .method(originalRequest.method(), originalRequest.body())
            .build()

        return chain.proceed(request)
    }
}
