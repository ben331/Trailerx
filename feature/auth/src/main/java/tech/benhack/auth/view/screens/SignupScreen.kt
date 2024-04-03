package tech.benhack.auth.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import tech.benhack.auth.R
import tech.benhack.auth.view.components.SignupTextField
import tech.benhack.auth.view.components.TrailerxImageHeader2
import tech.benhack.ui.components.SecondaryButton
import tech.benhack.ui.helpers.FormValidator
import tech.benhack.ui.theme.Gray800
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun SignupScreen(
    isLoading:Boolean,
    onRegister:(name:String, email:String, password:String)->Unit,
    onBack:()->Unit,
){
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var signupEnabled by rememberSaveable { mutableStateOf(false) }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf("") }
    var nameError by rememberSaveable { mutableStateOf("") }
    var firstOnCreate = true

    val background =
        if (!isLoading) TrailerxTheme.colorScheme.background
        else Color.Gray.copy(alpha = 0.8f)

    ConstraintLayout (
        modifier = Modifier
            .background(background)
            .fillMaxSize()
    ) {

        val (arrowBack, content) = createRefs()

        IconButton(
            modifier = Modifier.constrainAs(arrowBack){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
            onClick = { onBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                tint = TrailerxTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(content) {
                    top.linkTo(arrowBack.bottom, 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                TrailerxImageHeader2( modifier = Modifier.padding(vertical = 30.dp))
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = stringResource(id = R.string.create_account),
                    style = trailerxTypography.titleLarge,
                    color = TrailerxTheme.colorScheme.onBackground
                )
                SignupTextField(
                    modifier = Modifier
                        .onFocusChanged {
                            if(!firstOnCreate){
                                nameError =
                                    if (!it.hasFocus && name.isBlank()) "Campo requerido" else ""
                            }
                        }
                        .padding(vertical = 10.dp),
                    text = stringResource(id = R.string.name),
                    value = name,
                    error = nameError,
                    onValueChange = { value ->
                        name = value
                        signupEnabled = FormValidator.validateSignUp(name, email, password)
                    },
                )
                SignupTextField(
                    modifier = Modifier
                        .onFocusChanged {
                            if(!firstOnCreate){
                                emailError =
                                    if (!it.hasFocus && !FormValidator.validateEmail(email)) "Correo Inválido" else ""
                            }
                        }
                        .padding(vertical = 10.dp),
                    text = stringResource(id = R.string.email),
                    value = email ,
                    error = emailError,
                    keyboardType = KeyboardType.Email,
                    onValueChange = { value ->
                        email = value
                        signupEnabled = FormValidator.validateSignUp(name, email, password)
                    }
                )
                SignupTextField(
                    modifier = Modifier
                        .onFocusChanged {
                            if(!firstOnCreate){
                                passwordError =
                                    if (!it.hasFocus && !FormValidator.validatePassword(password)) "Contraseña Inválido" else ""
                            }else {
                                firstOnCreate = false
                            }
                        }
                        .padding(vertical = 10.dp),
                    text = stringResource(id = R.string.password),
                    value = password ,
                    error = passwordError,
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                    showPassword = showPassword,
                    onClickTrailingIcon = { showPassword = !showPassword },
                    onValueChange = { value ->
                        password = value
                        signupEnabled = FormValidator.validateSignUp(name, email, password)
                    }
                )
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = stringResource(id = R.string.password_rule),
                    style = trailerxTypography.labelSmall,
                    color = TrailerxTheme.colorScheme.onBackground
                )
                SecondaryButton(
                    modifier = Modifier
                        .width(280.dp)
                        .height(50.dp),
                    text = stringResource(id = R.string.accept),
                    enabled = signupEnabled && !isLoading
                ) { onRegister(name, email, password) }

                Row(
                    modifier = Modifier
                        .width(280.dp)
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if(isLoading) {
                        CircularProgressIndicator(
                            color = TrailerxTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun SignupScreenPreview(){
    TrailerxTheme {
        SignupScreen(
            isLoading = false,
            onRegister = { _, _, _ -> },
            onBack = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showSystemUi = true,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun SignupScreenNightPreview(){
    TrailerxTheme {
        SignupScreen(
            isLoading = true,
            onRegister = { _, _, _ -> },
            onBack = {}
        )
    }
}