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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.common.CategoryType
import tech.benhack.common.Constants
import tech.benhack.movies.model.MovieItem
import tech.benhack.ui.components.PrimaryButton
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun SectionProfile(
    title:String,
    movies:List<MovieItem>,
    modifier:Modifier = Modifier,
    showDescription:Boolean = false,
    description:String = "",
    listener: MovieProfileListener?,
    showBtn:Boolean = false,
    textButton:String = "",
    categoryType: CategoryType,
    onClick:()->Unit = {},
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
        if(showDescription){
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = description,
                style = trailerxTypography.bodyMedium,
                color = TrailerxTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(movies){ movie ->
                MovieProfile(
                    id = movie.id,
                    title = movie.title ?: "- - - -",
                    stars = movie.popularity.toString(),
                    imageUrl = Constants.IMAGES_BASE_URL + movie.backdropPath,
                    listener = listener,
                    category = categoryType
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if(showBtn){
            PrimaryButton(text = textButton) { onClick() }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun ItemMovieListPreview(){
    TrailerxTheme {
        SectionProfile(
            title = "Default Section Title",
            movies = emptyList(),
            showDescription = true,
            description = "Crea una lista de seguimiento para no perderte niguna película!",
            listener = null,
            showBtn = true,
            textButton = "Empieza tu lista de seguimiento",
            categoryType = CategoryType.HISTORY_MOVIES
        ) {

        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ItemMovieListPreviewNight(){
    TrailerxTheme {
        SectionProfile(
            title = "Default Section Title",
            movies = emptyList(),
            showDescription = true,
            description = "Crea una lista de seguimiento para no perderte niguna película!",
            listener = null,
            showBtn = true,
            textButton = "Empieza tu lista de seguimiento",
            categoryType = CategoryType.HISTORY_MOVIES
        ) {

        }
    }
}