package com.example.chatapp.addRoom

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.chatapp.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.addRoom.ui.theme.ui.theme.ChatAppTheme
import com.example.chatapp.login.LoginActivity
import com.example.chatapp.login.chatAuthTextField

class AddRoomActivity : ComponentActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme

                AddRoomContent(navigator = this)
            }
        }
    }

    override fun navigateUp() {
        finish()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomContent(
    viewModel: AddRoomViewModel = viewModel(),
    navigator: Navigator
) {
    viewModel.navigator = navigator
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(onClick = { viewModel.navigateUp() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Icon Back"
                    )
                }
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.add_room_title),
                    style = TextStyle(color = colorResource(id = R.color.white), fontSize = 20.sp)
                )
                Spacer(modifier = Modifier)
            }

        },
        contentColor = colorResource(id = R.color.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.background),
                    contentScale = ContentScale.FillBounds
                )
                .padding(top = it.calculateTopPadding())
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.10F))
            AddRoomCard(
                modifier = Modifier
                    .fillMaxWidth(0.85F)
                    .align(Alignment.CenterHorizontally),
                navigator = navigator
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomCard(
    modifier: Modifier = Modifier,
    viewModel: AddRoomViewModel = viewModel(),
    navigator: Navigator
) {
    viewModel.navigator = navigator
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.white),
            disabledContainerColor = colorResource(
                id = R.color.white
            ),
            contentColor = colorResource(id = R.color.black)
        )
    ) {
        Text(
            text = stringResource(R.string.create_new_room),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = TextStyle(
                colorResource(id = R.color.black),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        )
        Image(
            painter = painterResource(id = R.drawable.add_room_image),
            contentDescription = "Add Room Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(160.dp)
                .padding(vertical = 16.dp),
            contentScale = ContentScale.FillWidth


        )
        chatAuthTextField(
            state = viewModel.titleState,
            lable = stringResource(id = R.string.room_title),
            errorState = viewModel.titleErrorState
        )
        ExposedDropdownMenuBox(
            expanded = viewModel.isExpanded.value,
            onExpandedChange = {
                viewModel.isExpanded.value = !viewModel.isExpanded.value
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                readOnly = true,
                value = viewModel.selectedItem.value.name ?: "",
                onValueChange = {},
                label = { Text("Room Category") },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = viewModel.selectedItem.value.imageId!!),
                        contentDescription = "",
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                    )
                },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.isExpanded.value) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(containerColor = Color.Transparent),
            )
            ExposedDropdownMenu(
                expanded = viewModel.isExpanded.value,
                onDismissRequest = { viewModel.isExpanded.value = false }) {
                viewModel.categoriesList.forEach { category ->
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = category.imageId!!),
                                contentDescription = "Room Categeory Image",
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = category.name ?: "")
                        }
                    }, onClick = {
                        viewModel.selectedItem.value = category
                        viewModel.isExpanded.value = false
                    })
                }
            }
        }
        chatAuthTextField(
            state = viewModel.descriptionState,
            lable = stringResource(id = R.string.room_desc),
            errorState = viewModel.descriptionErrorState
        )
        Button(
            onClick = {
                viewModel.addRoomToFirestore()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.blue), contentColor = colorResource(
                    id = R.color.white
                )
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Create")
        }
    }
    LoadingDialog()
    ChatAlertDialog(navigator = navigator)
}

@Composable
fun LoadingDialog(viewModel: AddRoomViewModel = viewModel()) {
    if (viewModel.showLoading.value)
        Dialog(onDismissRequest = { }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .background(
                        color = colorResource(id = R.color.white),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp),
                    color = colorResource(id = R.color.blue)
                )
            }
        }
}


@Composable
fun ChatAlertDialog(viewModel: AddRoomViewModel = viewModel(), navigator: Navigator) {
    viewModel.navigator = navigator
    if (viewModel.message.value.isNotEmpty())
        AlertDialog(

            onDismissRequest = {
                viewModel.message.value = ""
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.message.value = ""
                    viewModel.navigateUp()
                }) { Text(text = "OK") }
            },
            text = { Text(text = viewModel.message.value) },
            modifier = Modifier.fillMaxWidth()
        )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    ChatAppTheme {

        AddRoomContent(navigator = object : Navigator {
            override fun navigateUp() {
            }
        })
    }
}