package com.globant.imdb.domain.model

import com.globant.imdb.data.database.entities.movie.VideoEntity
import com.globant.imdb.data.model.movies.VideoModel

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

fun VideoModel.toDomain():VideoItem{
    return VideoItem(
        id,
        iso6391,
        iso31661,
        name,
        key,
        site,
        size,
        type,
        official,
        publishedAt
    )
}

fun VideoEntity.toDomain():VideoItem{
    return VideoItem(
        id,
        iso6391,
        iso31661,
        name,
        key,
        site,
        size,
        type,
        official,
        publishedAt
    )
}