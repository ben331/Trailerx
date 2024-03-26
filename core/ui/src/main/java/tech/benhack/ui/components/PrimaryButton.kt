package tech.benhack.ui.components

import android.util.Log
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.ui.theme.Gray100
import tech.benhack.ui.theme.Gray500
import tech.benhack.ui.theme.Gray800
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxShapes
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isDisabled: Boolean = false,
) {
    var containerColor:Color = Gray800
    var textColor:Color = Gray100

    if(isDisabled){
        containerColor = Gray500
        textColor = Color.White
    }

    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        shape = trailerxShapes.small
    ) {
        Text(
            text = text,
            style = trailerxTypography.titleLarge,
            color = textColor
        )
    }
}

@Preview
@Composable
fun PrimaryButtonPreview(){
    TrailerxTheme {
        PrimaryButton(
            modifier = Modifier.width(250.dp),
            text = "Login",
            onClick = { Log.d("PrimaryButton", "onClick called") },
            isDisabled = false
        )
    }
}