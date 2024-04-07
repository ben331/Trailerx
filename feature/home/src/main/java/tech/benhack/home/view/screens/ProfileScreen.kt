package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tech.benhack.common.CategoryType
import tech.benhack.home.model.SectionProfileItem
import tech.benhack.home.view.components.MovieProfileListener
import tech.benhack.home.view.components.SectionProfile
import tech.benhack.ui.theme.DeepOrange900
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow200
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.Yellow700

@Composable
fun ProfileScreen(
    isGuest:Boolean,
    sections:List<SectionProfileItem>,
    profileImgUrl:String,
    onMenuClick:()->Unit,
    onLogout:()->Unit,
    listener: MovieProfileListener?
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = profileImgUrl,
                contentDescription = "profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                DeepOrange900,
                                Yellow700,
                                Yellow400,
                                Yellow200
                            )
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .border(2.dp, Yellow400, CircleShape)
            )
            IconButton(onClick = { onMenuClick() }) {
                Icon(
                    Icons.Filled.Menu, contentDescription = "Menu",
                    tint = TrailerxTheme.colorScheme.primary
                )
            }
        }
        if(!isGuest){
            sections.forEach { section ->
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
            Spacer(modifier = Modifier.height(160.dp))
        }else{
            GuestProfileScreen( onLogout = onLogout)
        }
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