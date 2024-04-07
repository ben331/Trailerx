package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import tech.benhack.common.Constants
import tech.benhack.home.R
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.movieShape
import tech.benhack.ui.theme.trailerxTypography

interface MovieHomeListener {
    fun showDetails(id: Int)
    fun bookmarkAction(id: Int)
    fun showInfo(id: Int)
}

@Composable
fun MovieHome(
    id: Int,
    title: String,
    stars: String,
    imageUrl: String,
    listener: MovieHomeListener?,
    modifier:Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .height(190.dp)
            .width(100.dp)
            .clickable { listener?.showDetails(id) }
            .background(
                TrailerxTheme.colorScheme.background,
                movieShape
            ),
    ) {

        val (bookmarkAdd, imgMovie, iconStar, labelStars, movieName, iconInfo) = createRefs()

        AsyncImage(
            model = imageUrl,
            contentDescription = "$title image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth()
                .background(Yellow400)
                .constrainAs(imgMovie) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        Image(
            painter = painterResource(id = tech.benhack.ui.R.drawable.ic_bookmark),
            contentDescription = stringResource(id = R.string.btn_bookmark),
            modifier = Modifier
                .size(30.dp)
                .clickable { listener?.bookmarkAction(id) }
                .constrainAs(bookmarkAdd) {
                    start.linkTo(parent.start, 4.dp)
                    top.linkTo(parent.top)
                },
        )

        Image(
            modifier = Modifier
                .size(16.dp)
                .constrainAs(iconStar) {
                    top.linkTo(imgMovie.bottom, 4.dp)
                    start.linkTo(parent.start, 4.dp)
                },
            painter = painterResource(id = tech.benhack.ui.R.drawable.ic_star),
            contentDescription = stringResource(id = R.string.img_star)
        )

        Text(
            text = stars,
            style = trailerxTypography.bodySmall,
            color = TrailerxTheme.colorScheme.secondary,
            modifier = Modifier
                .constrainAs(labelStars) {
                    top.linkTo(imgMovie.bottom, 4.dp)
                    start.linkTo(iconStar.end, 4.dp)
                }
        )

        Text(
            text = title,
            style = trailerxTypography.bodySmall.copy(fontSize = 10.sp),
            color = TrailerxTheme.colorScheme.onBackground,
            textAlign = TextAlign.Left,
            maxLines = 2,
            modifier = Modifier
                .width(70.dp)
                .height(24.dp)
                .constrainAs(movieName) {
                    top.linkTo(iconStar.bottom)
                    start.linkTo(parent.start, 4.dp)
                    end.linkTo(iconInfo.start)
                    bottom.linkTo(parent.bottom)
                }
        )

        Image(
            painter = painterResource(id = tech.benhack.ui.R.drawable.ic_info),
            contentDescription = stringResource(id = R.string.btn_bookmark),
            modifier = Modifier
                .size(16.dp)
                .clickable { listener?.showInfo(id) }
                .constrainAs(iconInfo) {
                    end.linkTo(parent.end, 4.dp)
                    bottom.linkTo(parent.bottom, 4.dp)
                },
        )

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ItemMoviePreview() {
    TrailerxTheme {
        MovieHome(
            id = 1,
            title = "Short",
            stars = "4.0",
            imageUrl = "${Constants.IMAGES_BASE_URL}/oe7mWkvYhK4PLRNAVSvonzyUXNy.jpg",
            listener = null
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemMoviePreviewNight() {
    TrailerxTheme {
        MovieHome(
            id = 1,
            title = "This is a title too long",
            stars = "5.0",
            imageUrl = "",
            listener = null,
        )
    }
}