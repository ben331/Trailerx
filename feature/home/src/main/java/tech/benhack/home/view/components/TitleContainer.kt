package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.trailerxShapes
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun TitleContainer(
    text:String,
    modifier: Modifier = Modifier,
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(28.dp)
                .width(8.dp)
                .background(
                    color = Yellow400,
                    shape = trailerxShapes.small
                )
        )
        Text(
            text = text,
            style = trailerxTypography.labelMedium,
            color = TrailerxTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF
)
@Composable
fun TitleContainerPreview(){
    TrailerxTheme {
        TitleContainer(
            "Default Section Title"
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TitleContainerPreviewNight(){
    TrailerxTheme {
        TitleContainer("Default Section Title")
    }
}