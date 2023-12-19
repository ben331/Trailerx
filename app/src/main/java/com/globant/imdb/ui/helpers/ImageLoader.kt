package com.globant.imdb.ui.helpers

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.globant.imdb.R
import javax.inject.Inject


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
            .asGif()
            .load(imageUrl)
            .apply(movieOptions)
            .into(view)
    }

    fun preLoadImage(context: Context, imageUrl:String? ) {
        Glide
            .with(context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .preload()
    }
}