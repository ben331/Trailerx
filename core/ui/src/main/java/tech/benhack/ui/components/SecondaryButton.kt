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
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Gray800,
            contentColor = Gray100,
            disabledContainerColor = Gray500,
            disabledContentColor = Color.White,
        ),
        shape = trailerxShapes.small
    ) {
        Text(
            text = text,
            style = trailerxTypography.titleLarge
        )
    }
}

@Preview
@Composable
fun SecondaryButtonPreview(){
    TrailerxTheme {
        SecondaryButton(
            modifier = Modifier.width(250.dp),
            text = "Login",
            onClick = { Log.d("PrimaryButton", "onClick called") },
            enabled = true
        )
    }
}