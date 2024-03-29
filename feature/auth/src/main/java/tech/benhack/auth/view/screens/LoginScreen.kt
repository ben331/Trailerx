package tech.benhack.auth.view.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.benhack.auth.R
import tech.benhack.auth.view.components.LoginTextField
import tech.benhack.auth.view.components.TrailerxImageHeader
import tech.benhack.ui.components.SecondaryButton
import tech.benhack.ui.helpers.FormValidator
import tech.benhack.ui.theme.Gray800
import tech.benhack.ui.theme.TrailerxTheme
import tech.benhack.ui.theme.Yellow400
import tech.benhack.ui.theme.Yellow700
import tech.benhack.ui.theme.trailerxTypography

@Composable
fun LoginScreen(
    isLoading:Boolean,
    onForgotPassword:(email:String)->Unit,
    onLoginWithEmailAndPassword:(email:String, password:String)->Unit,
    onLoginWithGoogle:()->Unit,
    onRegister:()->Unit,
    onLoginAsGuest:()->Unit
){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var loginEnabled by rememberSaveable { mutableStateOf(false) }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf("") }
    
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Yellow400),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TrailerxImageHeader()
            LoginTextField(
                modifier = Modifier
                    .onFocusChanged {
                        passwordError = if (it.hasFocus && !FormValidator.validateEmail(email)) {
                            "Contraseña Invalida"
                        }else{
                            ""
                        }
                    },
                text = stringResource(id = R.string.email),
                value = email,
                error = emailError
            ) { value ->
                email = value
                loginEnabled = FormValidator.validateLogin(email, password)
            }
            LoginTextField(
                modifier = Modifier
                    .onFocusChanged {
                        emailError = if (it.hasFocus && !FormValidator.validateEmail(email)) {
                            "Correo Invalido"
                        }else{
                            ""
                        }
                    },
                text = stringResource(id = R.string.password),
                value = password,
                error = passwordError,
                isPassword = true,
                showPassword = showPassword,
                onClickTrailingIcon = {showPassword = !showPassword},
            ) { value ->
                password = value
                loginEnabled = FormValidator.validateLogin(email, password)
            }
            Button(onClick = { onForgotPassword(email) }) {
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
            ) { onLoginWithEmailAndPassword(email, password) }
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
                onClick = { onLoginWithGoogle() }
            ) {
                Image(painterResource(id = tech.benhack.ui.R.drawable.ic_google), contentDescription = "Google")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.has_account),
                style = trailerxTypography.displayMedium,
                color = Gray800
            )
            Button(onClick = { onRegister() }) {
                Text(
                    text = stringResource(id = R.string.register),
                    style = trailerxTypography.labelMedium,
                    color = Gray800
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { onLoginAsGuest() }) {
                Text(
                    text = stringResource(id = R.string.guest),
                    style = trailerxTypography.labelMedium,
                    color = Gray800
                )
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
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun LoginScreenPreview(){
    TrailerxTheme {
        LoginScreen(
            false,
            {},
            {_,_->},
            {},
            {},
            {}
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
fun LoginScreenNightPreview(){
    TrailerxTheme {
        LoginScreen(
            true,
            {},
            {_,_->},
            {},
            {},
            {}
        )
    }
}