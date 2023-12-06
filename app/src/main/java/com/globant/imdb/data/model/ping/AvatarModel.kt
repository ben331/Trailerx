package com.globant.imdb.data.model.ping

import com.google.gson.annotations.SerializedName

data class AvatarModel(
    @SerializedName("gravatar") val gravatar:GravatarModel?,
    @SerializedName("tmdb") val tmbd:TmdbModel?
)
