package com.globant.imdb.ui.helpers

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class ImageLoader {
    companion object {
        fun renderImageCenterCrop(context: Context, imageUrl:String?, view:ImageView ) {
            Glide
                .with(context)
                .load(imageUrl)
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
        }

        fun renderImageCenterCrop(context: Context, imageUrl:Uri?, view:ImageView ) {
            Glide
                .with(context)
                .load(imageUrl)
                .fitCenter()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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
}