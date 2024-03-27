package tech.benhack.auth.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import tech.benhack.ui.R

@Composable
fun TrailerxImageHeader(){
    Image(
        painter = painterResource(id = R.drawable.ic_trailerx),
        contentDescription = stringResource(id = R.string.app_name),
        modifier = Modifier
            .width(250.dp)
            .height(80.dp)
    )
}