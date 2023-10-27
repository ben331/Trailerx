package com.globant.imdb.data.model

import com.google.gson.annotations.SerializedName

data class MovieList(
    val page:Int,
    var results:List<Movie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)
