package com.globant.imdb.domain

import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.Video

class GetOfficialTrailerUseCase {
    private val repository = IMDbRepository()

    suspend operator fun invoke(movieId:Int):String? {
        var officialTrailer:Video? = null
        val videoList = repository.getTrailers(movieId)
        if(videoList.isNotEmpty()){
            officialTrailer = videoList.find {
                    it.official
                &&  it.site == RetrofitHelper.YOUTUBE_SITE
                &&  it.name == RetrofitHelper.OFFICIAL_NAME
            }
        }
        return if(officialTrailer!=null){
            RetrofitHelper.getYoutubeIframe(officialTrailer.key)
        }else{
            null
        }
    }
}