package com.globant.imdb.core

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGES_BASE_URL = "https://image.tmdb.org/t/p/original"
    private const val TEMPLATE_YOUTUBE_IFRAME = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/{movieKey}?amp;controls=0\" title=\"YouTube video player\" frameborder=\"1\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
    const val OFFICIAL_NAME = "Official Trailer"
    const val YOUTUBE_SITE = "YouTube"

    lateinit var authToken:String
    fun getRetrofit(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $authToken")
                .build()
            Log.e("----->", newRequest.toString())
            chain.proceed(newRequest)
        }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getYoutubeIframe(movieKey:String):String{
        return TEMPLATE_YOUTUBE_IFRAME.replace("{movieKey}", movieKey)
    }
}
