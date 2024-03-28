package tech.benhack.auth.view.components

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import tech.benhack.ui.R
import tech.benhack.ui.theme.DeepOrange900
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxShapes
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun SignupTextField(
    text:String,
    value:String,
    modifier: Modifier = Modifier,
    isError:Boolean = false,
    isPassword:Boolean = false,
    showPassword:Boolean = false,
    keyboardType:KeyboardType = KeyboardType.Text,
    onClickTrailingIcon:()->Unit = {},
    onValueChange:(value:String)->Unit,
) {

    val loginTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = TrailerxTheme.colorScheme.onSurface,
        unfocusedTextColor = TrailerxTheme.colorScheme.secondary,
        disabledTextColor =  TrailerxTheme.colorScheme.secondary,
        errorTextColor = TrailerxTheme.colorScheme.secondary,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedBorderColor = TrailerxTheme.colorScheme.onSurface,
        unfocusedBorderColor = TrailerxTheme.colorScheme.secondary,
        disabledBorderColor = TrailerxTheme.colorScheme.secondary,
        errorBorderColor = DeepOrange900,
        cursorColor = TrailerxTheme.colorScheme.onBackground,
        errorCursorColor = TrailerxTheme.colorScheme.onBackground,
        focusedTrailingIconColor = TrailerxTheme.colorScheme.onSurface,
        unfocusedTrailingIconColor = TrailerxTheme.colorScheme.secondary,
        disabledTrailingIconColor = TrailerxTheme.colorScheme.secondary,
        errorTrailingIconColor = DeepOrange900,
        focusedLabelColor = TrailerxTheme.colorScheme.onSurface,
        unfocusedLabelColor = TrailerxTheme.colorScheme.secondary,
        disabledLabelColor = TrailerxTheme.colorScheme.secondary,
        errorLabelColor = TrailerxTheme.colorScheme.secondary,
        focusedSupportingTextColor = Color.Transparent,
        unfocusedSupportingTextColor = Color.Transparent,
        disabledSupportingTextColor = Color.Transparent,
        errorSupportingTextColor = DeepOrange900,
    )

    OutlinedTextField(
        modifier = modifier,
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
        label = {
            Text(
                text = text,
                style = trailerxTypography.labelSmall
            )
        },
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
fun SignupTextFieldTextFieldPreview(){
    TrailerxTheme {
        SignupTextField(
            text = "Name",
            value = "bengi-silva@hotmail.com",
            isError = false,
        ) { _ ->

        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0x00000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SignupTextFieldTextFieldPreviewNight(){
    var showPassword = false

    TrailerxTheme {
        SignupTextField(
            text = "Password",
            value = "password",
            isError = false,
            isPassword = true,
            showPassword = showPassword,
            onClickTrailingIcon = { showPassword = !showPassword }
        ) { _ ->

        }
    }
}