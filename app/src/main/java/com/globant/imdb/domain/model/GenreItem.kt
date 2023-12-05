package com.globant.imdb.domain.model

import com.globant.imdb.data.model.movies.GenreModel

data class GenreItem (
    val id:Int = 0,
    val name:String = ""
)

fun GenreModel.toDomain(): GenreItem = GenreItem(id, name)
