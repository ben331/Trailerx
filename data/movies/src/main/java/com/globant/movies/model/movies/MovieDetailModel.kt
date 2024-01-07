package com.globant.imdb.data.model.movies

import com.globant.movies.model.movies.CollectionModel
import com.globant.movies.model.movies.GenreModel
import com.globant.movies.model.movies.ProductionCompanyModel
import com.globant.movies.model.movies.ProductionCountryModel
import com.globant.movies.model.movies.SpokenLanguageModel
import com.google.gson.annotations.SerializedName

data class MovieDetailModel (
    @SerializedName("adult")                    val adult: Boolean?,
    @SerializedName("belongs_to_collection")    val belongsToCollection: CollectionModel?,
    @SerializedName("backdrop_path")            val backdropPath: String?,
    @SerializedName("budget")                   val budget: Int?,
    @SerializedName("genres")                   val genres: List<GenreModel>? = emptyList(),
    @SerializedName("homepage")                 val homepage:String? = "",
    @SerializedName("id")                       val id: Int = 0,
    @SerializedName("imdb_id")                  val imdbId:String? = "",
    @SerializedName("original_language")        val originalLanguage: String? = "",
    @SerializedName("original_title")           val originalTitle: String? = "",
    @SerializedName("overview")                 val overview: String? = "",
    @SerializedName("popularity")               val popularity: Double? = 0.0,
    @SerializedName("poster_path")              val posterPath: String? = "",
    @SerializedName("production_companies")     val productionCompanies: List<ProductionCompanyModel>? = emptyList(),
    @SerializedName("production_countries")     val productionCountries: List<ProductionCountryModel>? = emptyList(),
    @SerializedName("release_date")             val releaseDate: String? = "",
    @SerializedName("revenue")                  val revenue: Int? = 0,
    @SerializedName("runtime")                  val runtime:Int? = 0,
    @SerializedName("spoken_languages")         val spokenLanguages: List<SpokenLanguageModel>? = emptyList(),
    @SerializedName("status")                   val status: String? = "",
    @SerializedName("tagline")                  val tagline:String? = "",
    @SerializedName("title")                    val title: String? = "",
    @SerializedName("video")                    val video: Boolean? = false,
    @SerializedName("vote_average")             val voteAverage: Double? = 0.0,
    @SerializedName("vote_count")               val voteCount: Int? = 0
)
