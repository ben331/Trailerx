package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import tech.benhack.home.R
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun MainTrailer(
    imageUrl:String,
    youtubeVideoId:String?,
    title:String,
    modifier:Modifier = Modifier,
){
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        val (videoPlayerRef, bigImageRef, smallImageRef, titleRef, labelRef) = createRefs()

        AsyncImage(
            model = imageUrl,
            contentDescription = "$title image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Yellow400)
                .constrainAs(bigImageRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        youtubeVideoId?.let {
            YoutubePlayer(
                lifecycleOwner = LocalLifecycleOwner.current,
                youtubeVideoId = youtubeVideoId,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .constrainAs(videoPlayerRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )

            AsyncImage(
                model = imageUrl,
                contentDescription = "$title image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(140.dp)
                    .width(100.dp)
                    .background(Yellow400)
                    .constrainAs(smallImageRef) {
                        top.linkTo(bigImageRef.bottom, (-80).dp)
                        start.linkTo(parent.start, 40.dp)
                    },
            )
        }

        Text(
            text = title,
            style = trailerxTypography.labelMedium,
            color = TrailerxTheme.colorScheme.onBackground,
            modifier = Modifier
                .constrainAs(titleRef) {
                    top.linkTo(bigImageRef.bottom, 4.dp)
                    start.linkTo(smallImageRef.end, 24.dp)
                }
        )
        Text(
            text = stringResource(id = R.string.trailer),
            style = trailerxTypography.bodyMedium,
            color = TrailerxTheme.colorScheme.onBackground,
            modifier = Modifier
                .constrainAs(labelRef) {
                    top.linkTo(titleRef.bottom, 4.dp)
                    start.linkTo(smallImageRef.end, 24.dp)
                }
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun MainTrailerPreview(){
    TrailerxTheme {
        MainTrailer(
            imageUrl = "",
            youtubeVideoId = "",
            title = "Dune: Part Two"
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun MainTrailerPreviewDark(){
    TrailerxTheme {
        MainTrailer(
            imageUrl = "",
            youtubeVideoId = "",
            title = "Dune: Part Two"
        )
    }
}