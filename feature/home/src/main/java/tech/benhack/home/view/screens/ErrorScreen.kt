package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.auth.R
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun ErrorScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.error_ups),
            style = trailerxTypography.titleSmall,
            color = TrailerxTheme.colorScheme.secondary
        )
        Image(
            painter = painterResource(id = tech.benhack.ui.R.drawable.ic_server_down),
            contentDescription = stringResource(id = R.string.server_error),
            modifier = Modifier
                .size(300.dp)
        )
        Text(
            text = stringResource(id = R.string.server_error),
            style = trailerxTypography.titleSmall,
            color = TrailerxTheme.colorScheme.secondary
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xDDDDDD
)
@Composable
fun ErrorScreenPreview(){
    TrailerxTheme {
        ErrorScreen()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ErrorScreenPreviewNight(){
    TrailerxTheme {
        ErrorScreen()
    }
}
