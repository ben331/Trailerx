package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import tech.benhack.ui.R
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.trailerxShapes
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun CardSynopsis(
    imageUrl:String,
    genre:String,
    stars:String,
    text:String,
    modifier:Modifier = Modifier,
){
    ConstraintLayout(
        modifier = modifier
    ) {

        val (imageRef, genreRef, starsRef, iconRef, synopsisRef) = createRefs()

        AsyncImage(
            model = imageUrl,
            contentDescription = "movie image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(130.dp)
                .width(90.dp)
                .background(
                    color = Yellow400,
                    shape = trailerxShapes.medium
                )
                .clip(trailerxShapes.medium)
                .constrainAs(imageRef) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start, 24.dp)
                }
        )
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = TrailerxTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            ),
            shape = trailerxShapes.medium,
            modifier = Modifier
                .constrainAs(genreRef) {
                    top.linkTo(imageRef.top)
                    start.linkTo(imageRef.end, 16.dp)
                }
        ) {
            Text(
                text = genre,
                style = trailerxTypography.bodySmall,
                color = TrailerxTheme.colorScheme.background,
                modifier = Modifier.padding(8.dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_star),
            contentDescription = stringResource(id = tech.benhack.home.R.string.img_star),
            modifier = Modifier
                .size(16.dp)
                .constrainAs(iconRef) {
                    top.linkTo(genreRef.top)
                    bottom.linkTo(genreRef.bottom)
                    start.linkTo(genreRef.end, 16.dp)
                },
        )
        Text(
            text = stars,
            style = trailerxTypography.bodySmall,
            color = TrailerxTheme.colorScheme.onSurface,
            modifier = Modifier
                .constrainAs(starsRef){
                    top.linkTo(iconRef.top)
                    start.linkTo(iconRef.end, 4.dp)
                }
        )
        Text(
            text = text,
            style = trailerxTypography.bodyLarge,
            color = TrailerxTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .wrapContentHeight()
                .constrainAs(synopsisRef) {
                    top.linkTo(genreRef.bottom, 16.dp)
                    start.linkTo(imageRef.end, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                }
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun CardSynopsisPreview(){
    TrailerxTheme {
        CardSynopsis(
            "",
            "horror",
            "4.5",
            "Super movie description"
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
fun CardSynopsisPreviewDark(){
    TrailerxTheme {
        CardSynopsis(
            "",
            "horror",
            "4.5",
            "Super movie description"
        )
    }
}