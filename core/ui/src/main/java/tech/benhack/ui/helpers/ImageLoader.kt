package tech.benhack.ui.helpers

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import tech.benhack.ui.R
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val PRELOAD_TIME = 3000L
class ImageLoader @Inject constructor(){

    private val movieOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_foreground)

    fun renderImageCenterCrop(context: Context, imageUrl:String?, view:ImageView ) {
        Glide
            .with(context)
            .load(imageUrl)
            .apply(movieOptions)
            .into(view)
    }

    fun renderImageCenterCrop(context: Context, imageUrl:Uri?, view:ImageView ) {
        Glide
            .with(context)
            .load(imageUrl)
            .apply(movieOptions)
            .into(view)
    }

    suspend fun preLoadImages(context: Context, imageUrls:List<String?>) {
        for (url in imageUrls){
            if(!url.isNullOrEmpty()){
                Glide
                    .with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .preload()
            }
        }
        delay(PRELOAD_TIME)
    }
}