package tech.benhack.movies.model.movies

import com.google.gson.annotations.SerializedName

data class MoviesListModel(
    @SerializedName("page")             val page:Int,
    @SerializedName("results")          var results:List<MovieModel>,
    @SerializedName("total_pages")      val totalPages: Int,
    @SerializedName("total_results")    val totalResults: Int
)
