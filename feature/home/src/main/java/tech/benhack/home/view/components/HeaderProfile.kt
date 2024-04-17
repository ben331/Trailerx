package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import tech.benhack.ui.theme.DeepOrange900
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow200
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.Yellow700

@Composable
fun HeaderProfile(
    profileImgUrl:String,
    onMenuClick:()->Unit,
    modifier:Modifier = Modifier,
){
    Row(
        modifier = modifier
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
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun HeaderProfilePreview(){
    TrailerxTheme {
        HeaderProfile(
            "",
            {}
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HeaderProfilePreviewDark(){
    TrailerxTheme {
        HeaderProfile(
            "",
            {}
        )
    }
}