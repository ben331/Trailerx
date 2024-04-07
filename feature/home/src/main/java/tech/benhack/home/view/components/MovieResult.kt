package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import tech.benhack.ui.theme.Gray500
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.trailerxTypography

interface MovieResultListener {
    fun showDetails(id: Int)
}

@Composable
fun MovieResult(
    id: Int,
    title: String,
    year:String,
    cast:String,
    imageUrl: String,
    listener: MovieResultListener?,
    modifier:Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .clickable { listener?.showDetails(id) }
            .background(
                TrailerxTheme.colorScheme.background
            ),
    ) {

        val (dividerRef, imgMovie, movieName, movieYear, castRef) = createRefs()

        HorizontalDivider(
            modifier = Modifier
                .constrainAs(dividerRef){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = Gray500,
            thickness = 4.dp
        )

        AsyncImage(
            model = imageUrl,
            contentDescription = "$title image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(130.dp)
                .width(90.dp)
                .background(Yellow400)
                .constrainAs(imgMovie) {
                    top.linkTo(dividerRef.top, 16.dp)
                    start.linkTo(parent.start)
                },
        )


        Text(
            text = title,
            style = trailerxTypography.titleMedium.copy(fontSize = 22.sp),
            color = TrailerxTheme.colorScheme.onSurface,
            textAlign = TextAlign.Left,
            maxLines = 2,
            modifier = Modifier
                .width(70.dp)
                .height(24.dp)
                .constrainAs(movieName) {
                    top.linkTo(parent.top, 24.dp)
                    start.linkTo(imgMovie.end, 24.dp)
                }
        )


        Text(
            text = year,
            style = trailerxTypography.titleMedium.copy(fontSize = 22.sp),
            color = TrailerxTheme.colorScheme.onSurface,
            modifier = Modifier
                .constrainAs(movieYear) {
                    top.linkTo(movieName.bottom, 4.dp)
                    start.linkTo(imgMovie.end, 24.dp)
                }
        )


        Text(
            text = cast,
            style = trailerxTypography.titleMedium.copy(fontSize = 22.sp),
            color = TrailerxTheme.colorScheme.onSurface,
            modifier = Modifier
                .constrainAs(castRef) {
                    top.linkTo(movieYear.bottom, 24.dp)
                    start.linkTo(imgMovie.end, 24.dp)
                }
        )

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun ItemMovieProfilePreview() {
    TrailerxTheme {
        MovieResult(id = 1, title = "title", year = "2024" , cast = "cast", imageUrl = "", listener = null)
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemMovieProfilePreviewNight() {
    TrailerxTheme {
        MovieResult(id = 1, title = "title", year = "2024" , cast = "cast", imageUrl = "", listener = null)
    }
}