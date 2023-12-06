package com.globant.imdb.data.model.ping

import com.google.gson.annotations.SerializedName

data class GravatarModel(
    @SerializedName("hash") val hash:String = ""
)
