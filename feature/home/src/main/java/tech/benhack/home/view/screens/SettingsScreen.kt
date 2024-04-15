package tech.benhack.home.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.home.R
import tech.benhack.ui.components.DangerousButton
import tech.benhack.ui.components.PrimaryButton
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow700
import tech.benhack.ui.theme.trailerxTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isLoading:Boolean,
    isGuest:Boolean = false,
    onNavigateBack:()->Unit,
    onLogout:()->Unit,
    onDeleteAccount:()->Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
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
                            text = stringResource(id = R.string.settings),
                            maxLines = 1,
                            style = trailerxTypography.titleLarge,
                            color = TrailerxTheme.colorScheme.onBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description",
                                tint = TrailerxTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    text = stringResource(id = R.string.logout),
                    onClick = onLogout
                )
                if (!isGuest) {
                    DangerousButton(
                        text = stringResource(id = R.string.delete_account),
                        onClick = onDeleteAccount
                    )
                }
            }
        }

        if(isLoading){
            Box(modifier = Modifier
                .matchParentSize()
                .background(Color.Gray.copy(alpha = 0.8f)),
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(200.dp),
                color = Yellow700,
                strokeWidth = 16.dp
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
fun SettingsScreenPreview(){
    TrailerxTheme {
        SettingsScreen(
            false,
            false,
            {},
            {},
            {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
fun SettingsScreenPreviewDark(){
    TrailerxTheme {
        SettingsScreen(
            false,
            false,
            {},
            {},
            {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true
)
@Composable
fun SettingsScreenPreviewDarkLoading(){
    TrailerxTheme {
        SettingsScreen(
            true,
            false,
            {},
            {},
            {}
        )
    }
}