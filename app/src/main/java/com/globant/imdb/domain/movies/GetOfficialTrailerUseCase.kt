package com.globant.imdb.domain.movies

import com.globant.imdb.core.RetrofitHelper
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Video

class GetOfficialTrailerUseCase {
    private val repository = IMDbRepository()

    suspend operator fun invoke(movieId:Int, withControls:Boolean):String? {
        var officialTrailer: Video? = null
        val videoList = repository.getTrailers(movieId)
        if(videoList.isNotEmpty()){
            officialTrailer = videoList.find {
                    it.official
                &&  it.site == RetrofitHelper.YOUTUBE_SITE
                &&  it.name.contains(RetrofitHelper.OFFICIAL_NAME)
            }
            if(officialTrailer==null && videoList[0].site==RetrofitHelper.YOUTUBE_SITE){
                officialTrailer = videoList[0]
            }
        }
        return if(officialTrailer!=null){
            RetrofitHelper.getYoutubeIframe(officialTrailer.key, withControls)
        }else{
            null
        }
    }
}