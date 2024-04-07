package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.home.R
import tech.benhack.ui.components.PrimaryButton
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun GuestProfileScreen(
    onLogout:()->Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = tech.benhack.ui.R.drawable.ic_popcorn),
            contentDescription = stringResource(id = tech.benhack.auth.R.string.server_error),
            modifier = Modifier
                .size(400.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.invitation),
            textAlign = TextAlign.Center,
            style = trailerxTypography.labelMedium,
            color = TrailerxTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        PrimaryButton(
            text = stringResource(id = R.string.register),
            onClick = onLogout
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFF,
    showSystemUi = true
)
@Composable
fun GuestProfileScreenPreview(){
    TrailerxTheme {
        GuestProfileScreen {}
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GuestProfileScreenPreviewNight(){
    TrailerxTheme {
        GuestProfileScreen {}
    }
}