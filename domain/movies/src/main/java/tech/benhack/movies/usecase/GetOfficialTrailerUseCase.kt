package tech.benhack.movies.usecase

import tech.benhack.common.Constants
import tech.benhack.movies.model.VideoItem
import tech.benhack.movies.repository.MoviesRepository
import javax.inject.Inject

class GetOfficialTrailerUseCase @Inject constructor( private val repository: MoviesRepository){
    suspend operator fun invoke(movieId:Int, withControls:Boolean):String? {
        var officialTrailer: VideoItem? = null
        val videoList = repository.getTrailersFromApi(movieId)
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