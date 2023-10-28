package com.globant.imdb.data.model

import com.google.gson.annotations.SerializedName

data class Collection (
    @SerializedName("id")               val id:String,
    @SerializedName("name")             val name:String,
    @SerializedName("poster_path")      val posterPath:String,
    @SerializedName("backdrop_path")    val backdropPath: String?
)