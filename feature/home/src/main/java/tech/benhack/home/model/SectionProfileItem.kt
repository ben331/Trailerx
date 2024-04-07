package tech.benhack.home.model

import tech.benhack.common.CategoryType
import tech.benhack.movies.model.MovieItem

data class SectionProfileItem(
    val title:String,
    val categoryType: CategoryType,
    val movies:List<MovieItem> = emptyList(),
    val showDescription:Boolean = false,
    val description:String = "",
    val showBtn:Boolean = false,
    val textButton:String = "",
    val onClick:()->Unit = {},
)
