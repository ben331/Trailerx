package com.globant.movies.model.movies

import com.google.gson.annotations.SerializedName

data class VideoListModel(
    @SerializedName("id")       val id:String,
    @SerializedName("results")  val results:List<VideoModel>
)
