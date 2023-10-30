package com.globant.imdb.data.model

import com.google.gson.annotations.SerializedName

data class VideoList(
    @SerializedName("id")       val id:String,
    @SerializedName("results")  val results:List<Video>
)
