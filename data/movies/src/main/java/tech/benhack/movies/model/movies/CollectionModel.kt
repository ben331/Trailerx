package tech.benhack.movies.model.movies

import com.google.gson.annotations.SerializedName

data class CollectionModel (
    @SerializedName("id")               val id:String,
    @SerializedName("name")             val name:String,
    @SerializedName("poster_path")      val posterPath:String,
    @SerializedName("backdrop_path")    val backdropPath: String?
)