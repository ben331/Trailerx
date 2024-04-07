package tech.benhack.home.model

import tech.benhack.movies.model.MovieItem

data class SectionHomeItem(
    val title:String,
    val movies:List<MovieItem> = emptyList(),
    val showDescription:Boolean = false,
    val description:String = "",
    val showBtn:Boolean = false,
    val textButton:String = "",
    val onClick:()->Unit = {},
)
