package tech.benhack.home.view.components

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import tech.benhack.home.R
import tech.benhack.ui.theme.DeepOrange900
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxShapes
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun SearchTextField(
    value:String,
    modifier: Modifier = Modifier,
    onClickTrailingIcon:()->Unit,
    onValueChange:(value:String)->Unit,
) {

    val searchTextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = TrailerxTheme.colorScheme.tertiary,
        unfocusedTextColor = TrailerxTheme.colorScheme.tertiary,
        disabledTextColor =  TrailerxTheme.colorScheme.tertiary,
        errorTextColor = TrailerxTheme.colorScheme.tertiary,
        focusedContainerColor = TrailerxTheme.colorScheme.surfaceContainerLow,
        unfocusedContainerColor = TrailerxTheme.colorScheme.surfaceContainerLow,
        disabledContainerColor = TrailerxTheme.colorScheme.surfaceContainerLow,
        errorContainerColor = TrailerxTheme.colorScheme.surfaceContainerLow,
        cursorColor = TrailerxTheme.colorScheme.primary,
        errorCursorColor = TrailerxTheme.colorScheme.onSurface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLeadingIconColor = TrailerxTheme.colorScheme.tertiary,
        unfocusedLeadingIconColor = TrailerxTheme.colorScheme.tertiary,
        disabledLeadingIconColor = TrailerxTheme.colorScheme.tertiary,
        errorLeadingIconColor = TrailerxTheme.colorScheme.tertiary,
        focusedTrailingIconColor = TrailerxTheme.colorScheme.tertiary,
        unfocusedTrailingIconColor = TrailerxTheme.colorScheme.tertiary,
        disabledTrailingIconColor = TrailerxTheme.colorScheme.tertiary,
        errorTrailingIconColor = DeepOrange900,
        focusedLabelColor = Color.Transparent,
        unfocusedLabelColor = Color.Transparent,
        disabledLabelColor = Color.Transparent,
        errorLabelColor = Color.Transparent,
        focusedSupportingTextColor = Color.Transparent,
        unfocusedSupportingTextColor = Color.Transparent,
        disabledSupportingTextColor = Color.Transparent,
        errorSupportingTextColor = DeepOrange900,
        focusedPlaceholderColor = TrailerxTheme.colorScheme.tertiary,
        unfocusedPlaceholderColor = TrailerxTheme.colorScheme.tertiary,
    )

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = searchTextFieldColors,
        shape = trailerxShapes.medium,
        textStyle = trailerxTypography.titleSmall.copy(fontSize = 20.sp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                tint = TrailerxTheme.colorScheme.onSurface,
                contentDescription = stringResource(
                    id = tech.benhack.auth.R.string.error
                )
            )
        },
        trailingIcon = {
            if(value.isNotEmpty()) {
                IconButton(onClick = { onClickTrailingIcon() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        tint = TrailerxTheme.colorScheme.onSurface,
                        contentDescription = stringResource(
                            id = tech.benhack.auth.R.string.error
                        )
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_hint),
                style = trailerxTypography.titleSmall.copy(fontSize = 20.sp),
                color = TrailerxTheme.colorScheme.onSurface,
            )
        }
    )
}

@Preview
@Composable
fun SearchTextFieldPreview(){
    TrailerxTheme {
        SearchTextField(
            value = "",
            onClickTrailingIcon = {},
            onValueChange = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchTextFieldPreviewNight(){
    TrailerxTheme {
        SearchTextField(
            value = "bengi-silva.com",
            onClickTrailingIcon = {},
            onValueChange = {}
        )
    }
}