package tech.benhack.home.view.components

import android.content.res.Configuration
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import tech.benhack.ui.theme.TrailerxTheme

@Composable
fun YoutubePlayer (
    youtubeVideoId:String,
    lifecycleOwner: LifecycleOwner,
    modifier:Modifier = Modifier,
    showControls:Boolean = false,
    showCaptions:Boolean = false,
    showAnnotations:Boolean = false,
    showFullScreenButton:Boolean = false,
    handleEnterFullScreen:()->Unit = {},
    handleExitFullScreen:()->Unit = {},
) {

    var controls by rememberSaveable { mutableStateOf(0) }
    var fullScreen by rememberSaveable { mutableStateOf(0) }
    var captions by rememberSaveable { mutableStateOf(0) }
    var annotations by rememberSaveable { mutableStateOf(0) }

    controls = if(showControls) 1 else 0
    fullScreen = if(showFullScreenButton) 1 else 0
    captions = if(showCaptions) 1 else 0
    annotations = if(showAnnotations) 1 else 3

    val iframeOptions = remember {
        IFramePlayerOptions.Builder()
            .controls(controls)
            .rel(0)
            .ivLoadPolicy(annotations)
            .ccLoadPolicy(captions)
            .fullscreen(fullScreen)
            .build()
    }

    val youtubePlayerListener = remember {
        object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(youtubeVideoId, 0f)
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            val youTubePlayerView = YouTubePlayerView(context = context)
            youTubePlayerView.enableAutomaticInitialization = false

            youTubePlayerView.initialize(youtubePlayerListener, iframeOptions)

            youTubePlayerView.apply {
                lifecycleOwner.lifecycle.addObserver(this)
                addFullscreenListener( object : FullscreenListener {
                    override fun onEnterFullscreen(
                        fullscreenView: View,
                        exitFullscreen: () -> Unit
                    ) {
                        handleEnterFullScreen()
                    }
                    override fun onExitFullscreen() {
                        handleExitFullScreen()
                    }
                })
            }
        },
        update = {
            it.getYouTubePlayerWhenReady( object: YouTubePlayerCallback{
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(youtubeVideoId, 0f)
                }
            })
        }
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun YoutubePlayerPreview(){
    TrailerxTheme {
        YoutubePlayer(
            "",
            LocalLifecycleOwner.current
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun YoutubePlayerPreviewDark(){
    YoutubePlayer(
        "",
        LocalLifecycleOwner.current
    )
}