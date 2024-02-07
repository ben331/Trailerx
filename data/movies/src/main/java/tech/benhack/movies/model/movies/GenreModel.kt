package tech.benhack.movies.model.movies

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("id")       val id:Int = 0,
    @SerializedName("name")     val name:String = ""
)
