package com.globant.imdb.data.model.user

data class User(
    val email:String,
    val displayName:String,
    val watchList:List<String>,
)