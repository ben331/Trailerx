package com.globant.movies.model.movies

import com.google.gson.annotations.SerializedName

data class MoviesListDatesModel(
    @SerializedName("dates")            val dates: DatesModel,
    @SerializedName("page")             val page:Int,
    @SerializedName("results")          val results:List<MovieModel>,
    @SerializedName("total_pages")      val totalPages: Int,
    @SerializedName("total_results")    val totalResults: Int
)