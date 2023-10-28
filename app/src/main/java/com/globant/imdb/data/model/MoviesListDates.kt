package com.globant.imdb.data.model

import com.google.gson.annotations.SerializedName

data class MoviesListDates(
    @SerializedName("dates")            val dates:Dates,
    @SerializedName("page")             val page:Int,
    @SerializedName("results")          var results:List<Movie>,
    @SerializedName("total_pages")      val totalPages: Int,
    @SerializedName("total_results")    val totalResults: Int
)