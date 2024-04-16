package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.home.model.SectionHomeItem
import tech.benhack.home.view.components.MainTrailer
import tech.benhack.home.view.components.SectionHome
import tech.benhack.home.view.components.MovieHomeListener
import tech.benhack.ui.theme.TrailerxTheme

@Composable
fun HomeScreen(
    mainMovieTitle:String,
    mainImageUrl:String,
    mainVideoUrl:String,
    sections:List<SectionHomeItem>,
    listener: MovieHomeListener?
){
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ){
        MainTrailer(
            imageUrl = mainImageUrl,
            videoUrl = mainVideoUrl,
            title = mainMovieTitle,
        )
        sections.forEach { section ->
            SectionHome(
                title = section.title,
                movies = section.movies,
                listener = listener,
            )
        }
        Spacer(modifier = Modifier.height(160.dp))
    }

}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xEEEEEE
)
@Composable
fun HomeScreenPreview(){
    TrailerxTheme {
        HomeScreen(
            "Kung Fu Panda 4",
            "",
            "",
            listOf(
                SectionHomeItem(
                    title = "Próximamente",
                    movies = emptyList()
                ),
                SectionHomeItem(
                    title = "Upcoming",
                    movies = emptyList()
                )
            ),
            null
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
)
@Composable
fun HomeScreenPreviewNight(){
    TrailerxTheme {
        HomeScreen(
            "Madame Web",
            "",
            "",
            listOf(
                SectionHomeItem(
                    title = "Próximamente",
                    movies = emptyList()
                ),
                SectionHomeItem(
                    title = "Upcoming",
                    movies = emptyList()
                )
            ),
            null
        )
    }
}