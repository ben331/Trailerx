package com.globant.imdb.domain.movies

import com.globant.imdb.core.Constants
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.Video
import javax.inject.Inject

class GetOfficialTrailerUseCase @Inject constructor( val repository: IMDbRepository ) {
    suspend operator fun invoke(movieId:Int, withControls:Boolean):String? {
        var officialTrailer: Video? = null
        val videoList = repository.getTrailers(movieId)
        if(videoList.isNotEmpty()){
            officialTrailer = videoList.find {
                    it.official
                &&  it.site == Constants.YOUTUBE_SITE_NAME
                &&  it.name.contains(Constants.VIDEO_OFFICIAL_NAME)
            }
            if(officialTrailer==null && videoList[0].site==Constants.YOUTUBE_SITE_NAME){
                officialTrailer = videoList[0]
            }
        }
        return if(officialTrailer!=null){
            getYoutubeIframe(officialTrailer.key, withControls)
        }else{
            null
        }
    }

    private fun getYoutubeIframe(movieKey:String, withControls:Boolean):String{
        val path = if(withControls) movieKey else "$movieKey?amp"
        return Constants.YOUTUBE_IFRAME_TEMPLATE.replace("{movieKey}", path)
    }
}