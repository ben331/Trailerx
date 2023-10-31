package com.globant.imdb.data.model.movies

import com.google.gson.annotations.SerializedName

data class MoviesList(
    @SerializedName("page")             val page:Int,
    @SerializedName("results")          var results:List<Movie>,
    @SerializedName("total_pages")      val totalPages: Int,
    @SerializedName("total_results")    val totalResults: Int
)
