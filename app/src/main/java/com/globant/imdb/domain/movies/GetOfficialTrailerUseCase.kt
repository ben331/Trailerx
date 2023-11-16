package com.globant.imdb.domain.movies

import com.globant.imdb.core.Constants
import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.model.movies.VideoModel
import javax.inject.Inject

class GetOfficialTrailerUseCase @Inject constructor( private val repository:IMDbRepository ){
    suspend operator fun invoke(movieId:Int, withControls:Boolean):String? {
        var officialTrailer: VideoModel? = null
        val videoList = repository.getTrailers(movieId)
        if(videoList.isNotEmpty()){
            officialTrailer = videoList.find {
                    it.official
                &&  it.site == Constants.YOUTUBE_SITE
                &&  it.name.contains(Constants.OFFICIAL_NAME)
            }
            if(officialTrailer==null && videoList[0].site==Constants.YOUTUBE_SITE){
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
        return Constants.TEMPLATE_YOUTUBE_IFRAME.replace("{movieKey}", path)
    }
}