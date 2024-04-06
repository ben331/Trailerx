package tech.benhack.home.model

import tech.benhack.movies.model.MovieItem

data class SectionItem(
    val title:String,
    val movies:List<MovieItem> = emptyList()
)
