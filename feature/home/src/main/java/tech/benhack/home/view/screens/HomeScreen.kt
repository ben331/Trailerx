package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.home.model.SectionHomeItem
import tech.benhack.home.view.components.MainTrailer
import tech.benhack.home.view.components.MovieHomeListener
import tech.benhack.home.view.components.SectionHome
import tech.benhack.ui.helpers.NetworkState
import tech.benhack.ui.theme.TrailerxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isLoading:Boolean,
    uiState:NetworkState,
    onRefresh:()->Unit,
    mainMovieTitle:String,
    mainImageUrl:String,
    youtubeVideoId:String?,
    sections:List<SectionHomeItem>,
    listener: MovieHomeListener?
){
    val swipeRefreshState = rememberPullToRefreshState()

    if(swipeRefreshState.isRefreshing) {
        LaunchedEffect(true){
            onRefresh()
        }
    }

    if(!isLoading) {
        swipeRefreshState.endRefresh()
    }

    Box(Modifier.nestedScroll(swipeRefreshState.nestedScrollConnection)){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ){
            item {
                MainTrailer(
                    imageUrl = mainImageUrl,
                    offLineMode = uiState == NetworkState.Offline,
                    youtubeVideoId = youtubeVideoId,
                    title = mainMovieTitle,
                )
            }

            items(sections) { section ->
                SectionHome(
                    title = section.title,
                    movies = section.movies,
                    listener = listener,
                )
            }
            item {
                Spacer(modifier = Modifier.height(160.dp))
            }
        }
        PullToRefreshContainer(
            state = swipeRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            containerColor = TrailerxTheme.colorScheme.background,
            contentColor = TrailerxTheme.colorScheme.primary
        )
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
            false,
            NetworkState.Online,
            {},
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
            true,
            NetworkState.Online,
            {},
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