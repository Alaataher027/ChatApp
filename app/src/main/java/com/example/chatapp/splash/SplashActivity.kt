package com.example.chatapp.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.chatapp.R
import com.example.chatapp.home.HomeActivity
import com.example.chatapp.login.LoginActivity
import com.example.chatapp.register.RegisterActivity
import com.example.chatapp.ui.theme.ChatAppTheme

class SplashActivity : ComponentActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
//                Handler(Looper.getMainLooper()).postDelayed({
//                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }, 2000)
                splashContent(navigator = this@SplashActivity)
            }
        }
    }

    override fun navigateToHomeScreen() {
        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigateToLoginScreen() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun splashContent(
    viewModel: SpalshViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigator: Navigator
) {
    viewModel.navigator = navigator
    viewModel.navigate()
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (logo, signature) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.logo), contentDescription = "logo",
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize(0.4F)
        )
        Image(
            painter = painterResource(id = R.drawable.signature), contentDescription = "signature",
            modifier = Modifier
                .constrainAs(signature) {
                    top.linkTo(logo.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize(0.4F)
        )


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatAppTheme {

        splashContent(navigator = object: Navigator{
            override fun navigateToHomeScreen() {
            }

            override fun navigateToLoginScreen() {
            }
        })
    }
}