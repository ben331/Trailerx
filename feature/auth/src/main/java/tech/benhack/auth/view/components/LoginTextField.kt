package tech.benhack.auth.view.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.ui.R
import tech.benhack.ui.theme.DeepOrange900
import tech.benhack.ui.theme.Gray800
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxShapes
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun LoginTextField(
    text:String,
    value:String,
    modifier: Modifier = Modifier,
    isError:Boolean = false,
    isPassword:Boolean = false,
    showPassword:Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onClickTrailingIcon:()->Unit = {},
    onValueChange:(value:String)->Unit,
) {

    val loginTextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = TrailerxTheme.colorScheme.onBackground,
        unfocusedTextColor = TrailerxTheme.colorScheme.onBackground,
        disabledTextColor =  TrailerxTheme.colorScheme.onBackground,
        errorTextColor = TrailerxTheme.colorScheme.onBackground,
        focusedContainerColor = TrailerxTheme.colorScheme.surfaceContainer,
        unfocusedContainerColor = TrailerxTheme.colorScheme.surfaceContainer,
        disabledContainerColor = TrailerxTheme.colorScheme.surfaceContainer,
        errorContainerColor = TrailerxTheme.colorScheme.surfaceContainer,
        cursorColor = TrailerxTheme.colorScheme.onBackground,
        errorCursorColor = TrailerxTheme.colorScheme.onBackground,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedTrailingIconColor = TrailerxTheme.colorScheme.onSurface,
        unfocusedTrailingIconColor = TrailerxTheme.colorScheme.onSurface,
        disabledTrailingIconColor = TrailerxTheme.colorScheme.secondary,
        errorTrailingIconColor = DeepOrange900,
        focusedLabelColor = Color.Transparent,
        unfocusedLabelColor = Color.Transparent,
        disabledLabelColor = Color.Transparent,
        errorLabelColor = Color.Transparent,
        focusedSupportingTextColor = Color.Transparent,
        unfocusedSupportingTextColor = Color.Transparent,
        disabledSupportingTextColor = Color.Transparent,
        errorSupportingTextColor = DeepOrange900,
    )

    Column(
        modifier = modifier,
    ) {
        Text(
            text = text,
            color = Gray800,
            style = trailerxTypography.labelMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            colors = loginTextFieldColors,
            shape = trailerxShapes.medium,
            textStyle = trailerxTypography.displayMedium,
            visualTransformation = if(isPassword && !showPassword) {
                PasswordVisualTransformation()
            } else VisualTransformation.None ,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = {
                if(isError) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(
                            id = tech.benhack.auth.R.string.error
                        )
                    )
                } else if ( isPassword) {
                    IconButton(onClick = { onClickTrailingIcon() }) {
                        if(showPassword) {
                            Icon(
                                painter = painterResource(R.drawable.visibility),
                                contentDescription = stringResource(
                                    id = tech.benhack.auth.R.string.error
                                )
                            )
                        } else {
                            Icon(
                                painter = painterResource(R.drawable.visibility_off),
                                contentDescription = stringResource(
                                    id = tech.benhack.auth.R.string.error
                                )
                            )
                        }
                    }
                }
            },
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF6C700,
    showSystemUi = true
)
@Composable
fun LoginTextFieldPreview(){
    TrailerxTheme {
        LoginTextField(
            text = "Password",
            modifier = Modifier.fillMaxSize(),
            value = "password",
            isError = false,
            isPassword = true,
            showPassword = false
        ) { _ ->

        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF6C700,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoginTextFieldPreviewNight(){
    TrailerxTheme {
        LoginTextField(
            text = "Email",
            modifier = Modifier.fillMaxSize(),
            value = "bengi-silva.com",
            isError = true
        ) { _ ->

        }
    }
}