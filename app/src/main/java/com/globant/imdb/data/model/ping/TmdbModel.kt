package com.globant.imdb.data.model.ping

import com.google.gson.annotations.SerializedName

data class TmdbModel(
    @SerializedName("avatar_path") val avatarPath: String = ""
)
