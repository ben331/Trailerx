package tech.benhack.ui.components

import android.util.Log
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxShapes
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun DangerousButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = trailerxShapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = TrailerxTheme.colorScheme.error
        )
    ) {
        Text(
            text = text,
            style = trailerxTypography.titleLarge
        )
    }
}

@Preview
@Composable
fun DangerousButtonPreview(){
    TrailerxTheme {
        DangerousButton(
            modifier = Modifier.width(250.dp),
            text = "Login",
            onClick = { Log.d("PrimaryButton", "onClick called") }
        )
    }
}