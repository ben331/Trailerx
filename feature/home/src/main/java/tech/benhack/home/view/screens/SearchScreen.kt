package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.common.Constants
import tech.benhack.home.view.components.MovieResult
import tech.benhack.home.view.components.MovieResultListener
import tech.benhack.home.view.components.SearchTextField
import tech.benhack.movies.model.MovieItem
import tech.benhack.ui.helpers.NetworkState
import tech.benhack.ui.helpers.toYear
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow700

@Composable
fun SearchScreen(
    movies:List<MovieItem>,
    onQueryChange:(value:String)->Unit,
    uiState:NetworkState,
    listener:MovieResultListener?
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrailerxTheme.colorScheme.inverseSurface),
        contentAlignment = Alignment.Center
    ){

        var query by rememberSaveable { mutableStateOf("") }

        if(uiState != NetworkState.Offline){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .background(TrailerxTheme.colorScheme.background)
                            .height(100.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        SearchTextField(
                            value = query,
                            onValueChange = { value ->
                                query = value
                                onQueryChange(value)
                            },
                            onClickTrailingIcon = {
                                query = ""
                                onQueryChange("")
                            }
                        )
                    }
                }
                items(movies){ movieItem ->
                    MovieResult(
                        id = movieItem.id,
                        title = movieItem.title ?: "-----",
                        year = movieItem.releaseDate.toYear(),
                        cast = "",
                        imageUrl = Constants.IMAGES_BASE_URL + movieItem.backdropPath,
                        listener = listener,
                    )
                }
            }
        }else{
            ErrorScreen()
        }

        if(uiState == NetworkState.Loading){
            Box(modifier = Modifier
                .matchParentSize()
                .background(Color.Gray.copy(alpha = 0.8f)),
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(200.dp),
                color = Yellow700,
                strokeWidth = 16.dp
            )
        }
    }
}
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor =  0xFFFFFF
)
@Composable
fun SearchScreenPreview(){
    TrailerxTheme {
        SearchScreen(
            movies = emptyList(),
            onQueryChange = {},
            uiState = NetworkState.Online,
            listener = null
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor =  0xFFFFFF
)
@Composable
fun SearchScreenPreviewError(){
    TrailerxTheme {
        SearchScreen(
            movies = emptyList(),
            onQueryChange = {},
            uiState = NetworkState.Offline,
            listener = null
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchScreenPreviewNight(){
    TrailerxTheme {
        SearchScreen(
            movies = emptyList(),
            onQueryChange = {},
            uiState = NetworkState.Loading,
            listener = null
        )
    }
}