package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import tech.benhack.home.R
import tech.benhack.home.view.components.CardSynopsis
import tech.benhack.home.view.components.TitleContainer
import tech.benhack.ui.components.PrimaryButton
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.trailerxTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    movieId:Int,
    title:String,
    genre:String,
    stars:String,
    synopsis:String,
    originalTitle:String,
    releaseDate:String,
    imageUrl:String,
    onNavigateBack:()->Unit,
    onAddToWatchList:(movieId:Int)->Unit
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TrailerxTheme.colorScheme.background,
                    titleContentColor = TrailerxTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        style = trailerxTypography.titleLarge,
                        color = TrailerxTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
        ) {
            val (
                titleRef,
                originalTitleRef,
                releaseDateRef,
                imageRef,
                synopsisSectionRef,
                buttonRef,
            ) = createRefs()
            
            TitleContainer(
                text = title,
                modifier = Modifier
                    .constrainAs(titleRef){
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top)
                    }
            )
            Text(
                text =  originalTitle,
                style = trailerxTypography.bodySmall,
                color = TrailerxTheme.colorScheme.onBackground,
                modifier = Modifier
                    .constrainAs(originalTitleRef) {
                        start.linkTo(titleRef.start, 12.dp)
                        top.linkTo(titleRef.bottom, 8.dp)
                    }
            )
            Text(
                text =  releaseDate,
                style = trailerxTypography.bodySmall,
                color = TrailerxTheme.colorScheme.onSurface,
                modifier = Modifier
                    .constrainAs(releaseDateRef) {
                        start.linkTo(originalTitleRef.start, 4.dp)
                        top.linkTo(originalTitleRef.bottom, 8.dp)
                    }
            )
            AsyncImage(
                model = imageUrl,
                contentDescription = "$title image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(Yellow400)
                    .constrainAs(imageRef) {
                        top.linkTo(releaseDateRef.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )
            CardSynopsis(
                imageUrl = imageUrl,
                genre = genre,
                stars = stars,
                text = synopsis,
                modifier = Modifier.constrainAs(synopsisSectionRef) {
                    top.linkTo(imageRef.bottom, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            PrimaryButton(
                text = stringResource(id = R.string.add_to_watch_list),
                onClick = { onAddToWatchList(movieId) },
                modifier = Modifier
                    .constrainAs(buttonRef) {
                        top.linkTo(synopsisSectionRef.bottom, 18.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun MovieScreenPreview(){
    TrailerxTheme {
        MovieScreen(
            1,
            "Kung Fu Panda 4",
            "Adventure",
            "4.5",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            "Kung Fu Panda 4",
            "2024",
            "",
            {},
            {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
)
@Composable
fun MovieScreenPreviewDark(){
    TrailerxTheme {
        MovieScreen(
            2,
            "Kung Fu Panda 4",
            "Adventure",
            "4.5",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            "Kung Fu Panda 4",
            "2024",
            "",
            {},
            {}
        )
    }
}