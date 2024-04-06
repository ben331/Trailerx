package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.home.model.SectionItem
import tech.benhack.home.view.components.Section
import tech.benhack.home.view.components.MovieListener
import tech.benhack.ui.theme.TrailerxTheme

@Composable
fun HomeScreen(
    sections:List<SectionItem>,
    listener: MovieListener?
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        sections.forEach { section ->
            Section(
                title = section.title,
                movies = section.movies,
                listener = listener,
            )
        }
        Spacer(modifier = Modifier.height(160.dp))
        Text(text = "prueba")
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
            listOf(
                SectionItem(
                    title = "Próximamente",
                    movies = emptyList()
                ),
                SectionItem(
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
            listOf(
                SectionItem(
                    title = "Próximamente",
                    movies = emptyList()
                ),
                SectionItem(
                    title = "Upcoming",
                    movies = emptyList()
                )
            ),
            null
        )
    }
}