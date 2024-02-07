package tech.benhack.movies.model

data class VideoItem(
    val id:String,
    val iso6391:String,
    val iso31661:String,
    val name:String,
    val key:String,
    val site:String,
    val size:Int,
    val type:String,
    val official:Boolean,
    val publishedAt: String
)