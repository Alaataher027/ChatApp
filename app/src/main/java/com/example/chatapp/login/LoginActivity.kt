package com.example.chatapp.login

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint.Style
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.home.HomeActivity
import com.example.chatapp.login.ui.theme.ChatAppTheme
import com.example.chatapp.register.RegisterActivity
import com.example.chatapp.register.RegisterViewModel

class LoginActivity : ComponentActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme

                loginContent(navigator = this)

            }
        }
    }

    override fun openHomeActivity() {

        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun openRegisterActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginContent(
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigator: Navigator
) {
    viewModel.navigator = navigator
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = "Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.0.dp),
                style = TextStyle(
                    color = colorResource(id = R.color.white),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            )

        }) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.background),
                    contentScale = ContentScale.FillBounds //scaleType = fitXY
                )
        ) {
            Spacer(modifier = Modifier.fillMaxSize(0.35F))

            chatAuthTextField(
                state = viewModel.emailState,
                lable = "Email",
                errorState = viewModel.emailError
            )
            chatAuthTextField(
                state = viewModel.passwordState,
                lable = "Password",
                errorState = viewModel.passwordError,
                isPassword = true
            )
            Spacer(modifier = Modifier.fillMaxSize(0.45F))
            chatButton("Login") {
                viewModel.sendAuthDataToFirebase()

            }
            val context = LocalContext.current
            TextButton(onClick = {
                viewModel.navigateToRegisterScreen()
            }) {
                Text(text = "or Create an account")
            }
            loadingDialog()
            chatAlertDialog()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatAuthTextField(
    state: MutableState<String>,
    lable: String,
    errorState: MutableState<String>,
    isPassword: Boolean = false
) {

    TextField(
        value = state.value, onValueChange = { newValue ->
            state.value = newValue
        }, colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
        label = {
            Text(
                text = lable,
                style = TextStyle(
                    color = colorResource(id = R.color.gray),
                    fontSize = 14.sp
                ),
            )
        },
        isError = errorState.value.isNotEmpty(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
    )
    if (errorState.value.isNotEmpty()) {
        Text(
            text = errorState.value,
            style = TextStyle(color = Color.Red),
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
        )
    }

}

@Composable
fun chatButton(buttonText: String, onButtonClick: () -> Unit) {

    Button(
        onClick = {
            onButtonClick()
        }, colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(
                id = R.color.blue
            ),
            contentColor = colorResource(id = R.color.white)
        ),
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = buttonText,
            style = TextStyle(color = colorResource(id = R.color.white), fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.width(80.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "arrow"
        )
    }
}


@Composable
fun loadingDialog(viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    if (viewModel.showLoading.value) {
        androidx.compose.ui.window.Dialog(onDismissRequest = {}) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .background(
                        color = colorResource(id = R.color.white),
                        shape = RoundedCornerShape(8.dp)
                    ),
            ) {
                CircularProgressIndicator(color = colorResource(id = R.color.blue))

            }

        }
    }

}

@Composable
fun chatAlertDialog(viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    if (viewModel.message.value.isNotEmpty())
        AlertDialog(

            onDismissRequest = {
                viewModel.message.value = ""
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.message.value = ""
                }) { Text(text = "OK") }
            },
            text = { Text(text = viewModel.message.value) },
            modifier = Modifier.fillMaxWidth()
        )
}

@Preview(showBackground = true)
@Composable
fun Dialog() {
    chatAlertDialog()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    loginContent(navigator = object : Navigator {
        override fun openHomeActivity() {
        }

        override fun openRegisterActivity() {
        }

    })
}