package tech.benhack.auth.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.auth.R
import tech.benhack.auth.view.components.LoginTextField
import tech.benhack.auth.view.components.TrailerxImageHeader
import tech.benhack.ui.components.SecondaryButton
import tech.benhack.ui.theme.Gray500
import tech.benhack.ui.theme.Gray800
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun LoginScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Yellow400),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TrailerxImageHeader()
        LoginTextField(
            text = stringResource(id = R.string.email),
            value = "",
            onValueChange = {}
        )
        LoginTextField(
            text = stringResource(id = R.string.password),
            value = "",
            onValueChange = {}
        )
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                style = trailerxTypography.labelSmall,
                color = Gray800
            )
        }
        SecondaryButton(
            modifier = Modifier
                .width(280.dp)
                .height(50.dp),
            text = stringResource(id = R.string.login),
            enabled = false
        ) {
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.login_with),
            style = trailerxTypography.displayMedium,
            color = Gray800
        )
        Spacer(modifier = Modifier.height(20.dp))
        IconButton(
            modifier = Modifier
                .size(52.dp)
                .background(Color.White, shape = CircleShape)
                .padding(12.dp),
            onClick = {}
        ) {
            Image(painterResource(id = tech.benhack.ui.R.drawable.ic_google), contentDescription = "Google")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.has_account),
            style = trailerxTypography.displayMedium,
            color = Gray800
        )
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = stringResource(id = R.string.register),
                style = trailerxTypography.labelMedium,
                color = Gray800
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = stringResource(id = R.string.guest),
                style = trailerxTypography.labelMedium,
                color = Gray800
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun LoginScreenPreview(){
    TrailerxTheme {
        LoginScreen()
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun LoginScreenNightPreview(){
    TrailerxTheme {
        LoginScreen()
    }
}