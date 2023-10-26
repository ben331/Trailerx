package com.globant.imdb.data.local.room.model

data class UserModel (
    val displayName:String,
    val email:String,
    val password:String,
    val profileImage:String?
)