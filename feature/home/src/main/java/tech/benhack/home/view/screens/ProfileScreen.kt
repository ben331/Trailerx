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
import tech.benhack.common.CategoryType
import tech.benhack.home.model.SectionProfileItem
import tech.benhack.home.view.components.HeaderProfile
import tech.benhack.home.view.components.MovieProfileListener
import tech.benhack.home.view.components.SectionProfile
import tech.benhack.ui.theme.TrailerxTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    isLoading:Boolean,
    onRefresh:()->Unit,
    isGuest:Boolean,
    sections:List<SectionProfileItem>,
    profileImgUrl:String,
    onMenuClick:()->Unit,
    onLogout:()->Unit,
    listener: MovieProfileListener?
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
            item {
                HeaderProfile(
                    profileImgUrl = profileImgUrl,
                    onMenuClick = onMenuClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if(!isGuest){
                items(sections){ section ->
                    with(section){
                        SectionProfile(
                            title = title,
                            categoryType = categoryType,
                            movies = movies,
                            listener = listener,
                            description = description,
                            showDescription = showDescription,
                            textButton = textButton,
                            showBtn = showBtn,
                            onClick = onClick
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(160.dp))
                }
            }else{
                item {
                    GuestProfileScreen( onLogout = onLogout)
                }
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
fun ProfileScreenPreview(){
    TrailerxTheme {
        ProfileScreen(
            false,
            {},
            sections = listOf(
                SectionProfileItem(
                    title = "My list",
                    movies = emptyList(),
                    categoryType = CategoryType.WATCH_LIST_MOVIES
                ),
                SectionProfileItem(
                    title = "History",
                    movies = emptyList(),
                    categoryType = CategoryType.HISTORY_MOVIES
                )
            ),
            profileImgUrl = "",
            onMenuClick = {  },
            listener = null,
            isGuest = false,
            onLogout = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
)
@Composable
fun ProfileScreenPreviewNight(){
    TrailerxTheme {
        ProfileScreen(
            false,
            {},
            sections = listOf(
                SectionProfileItem(
                    title = "My list",
                    movies = emptyList(),
                    categoryType = CategoryType.WATCH_LIST_MOVIES
                ),
                SectionProfileItem(
                    title = "History",
                    movies = emptyList(),
                    categoryType = CategoryType.HISTORY_MOVIES
                )
            ),
            profileImgUrl = "",
            onMenuClick = {  },
            listener = null,
            isGuest = false,
            onLogout = {}
        )
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
)
@Composable
fun ProfileScreenPreviewNightGuest(){
    TrailerxTheme {
        ProfileScreen(
            false,
            {},
            sections = listOf(
                SectionProfileItem(
                    title = "My list",
                    movies = emptyList(),
                    categoryType = CategoryType.WATCH_LIST_MOVIES
                ),
                SectionProfileItem(
                    title = "History",
                    movies = emptyList(),
                    categoryType = CategoryType.HISTORY_MOVIES
                )
            ),
            profileImgUrl = "",
            onMenuClick = {  },
            listener = null,
            isGuest = true,
            onLogout = {}
        )
    }
}