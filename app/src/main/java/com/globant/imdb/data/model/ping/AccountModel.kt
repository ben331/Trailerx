package com.globant.imdb.data.model.ping

import com.google.gson.annotations.SerializedName

class AccountModel (
    @SerializedName("avatar") val avatarModel:AvatarModel? = null,
    @SerializedName("id") val id:String = "",
    @SerializedName("iso_639_1") val iso6391:String = "",
    @SerializedName("iso_3166_1") val iso31661:String = "",
    @SerializedName("name") val name:String = "",
    @SerializedName("include_adult") val includeAdult:String = "",
    @SerializedName("username") val username:String = ""
)