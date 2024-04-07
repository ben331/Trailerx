package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.common.Constants
import tech.benhack.movies.model.MovieItem
import tech.benhack.ui.theme.TrailerxTheme

@Composable
fun SectionHome(
    title:String,
    movies:List<MovieItem>,
    modifier:Modifier = Modifier,
    listener: MovieHomeListener?,
){
    Column(
        modifier = modifier
            .background(
                TrailerxTheme.colorScheme.surfaceContainerHigh
            )
            .padding(start = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TitleContainer(
            modifier = Modifier
                .fillMaxWidth(),
            text = title
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(movies){ movie ->
                MovieHome(
                    id = movie.id,
                    title = movie.title ?: "- - - -",
                    stars = movie.popularity.toString(),
                    imageUrl = Constants.IMAGES_BASE_URL + movie.backdropPath,
                    listener = listener,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun SectionHomePreview(){
    TrailerxTheme {
        SectionHome(
            title = "Default Section Title",
            movies = emptyList(),
            listener = null,
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SectionHomePreviewNight(){
    TrailerxTheme {
        SectionHome(
            title = "Default Section Title",
            movies = emptyList(),
            listener = null,
        )
    }
}