package tech.benhack.auth.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.ui.R
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.trailerxShapes

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

@Composable
fun TrailerxImageHeader2(
    modifier: Modifier = Modifier
){
    Image(
        modifier = modifier
            .width(100.dp)
            .height(60.dp)
            .background(Yellow400, shape = trailerxShapes.medium)
            .padding(vertical = 12.dp),
        painter = painterResource(id = R.drawable.ic_trailerx),
        contentDescription = stringResource(id = R.string.app_name)
    )
}

@Preview
@Composable
fun TrailerxImageHeaderPreview(){
    TrailerxImageHeader()
}

@Preview
@Composable
fun TrailerxImageHeader2Preview(){
    TrailerxImageHeader2()
}