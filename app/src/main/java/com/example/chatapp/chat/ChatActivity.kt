package com.example.chatapp.chat

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.R
import com.example.chatapp.addRoom.AddRoomCard
import com.example.chatapp.chat.ui.theme.ChatAppTheme
import com.example.chatapp.model.Constants
import com.example.chatapp.model.DataUtils
import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import java.text.SimpleDateFormat
import java.util.Date

class ChatActivity : ComponentActivity(), Navigator {
    lateinit var room: Room
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(Constants.EXTRA_ROOM, Room::class.java)!!
        else
            intent.getParcelableExtra(Constants.EXTRA_ROOM)!!

        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme

                //chatScreenContent(room, navigator = this)
                ChatScreenContent(room = Room(), navigator = this@ChatActivity)
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
fun ChatScreenContent(
    room: Room,
    viewModel: ChatViewModel = viewModel(),
    navigator: Navigator
) {
    viewModel.navigator = navigator
    viewModel.room = room
    viewModel.getMessagesFromFirestore()
    Scaffold(contentColor = colorResource(id = R.color.white),
        topBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    viewModel.navigateUp()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Icon back"
                    )
                }
                Text(
                    text = room.name ?: "",
                    style = TextStyle(fontSize = 22.sp)
                )// title of the room
                Spacer(modifier = Modifier.width(30.dp))
            }
        }, bottomBar = {
            ChatSendMessageBar()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painterResource(id = R.drawable.background),
                    contentScale = ContentScale.FillBounds
                )
                .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
        ) {
            ChatLazyColumn()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSendMessageBar(viewModel: ChatViewModel = viewModel()) {
    Row(
        modifier = Modifier.padding(bottom = 24.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = viewModel.messageFieldState.value,
            onValueChange = {
                viewModel.messageFieldState.value = it
            },
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomEnd = 0.dp,
                bottomStart = 0.dp,
                topEnd = 12.dp
            ),
            label = {
                Text(text = "Message")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.Transparent)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                viewModel.addMessageToFirestore()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.blue)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = stringResource(id = R.string.send))
            Spacer(modifier = Modifier.width(4.dp))
            Icon(painter = painterResource(id = R.drawable.baseline_send_24), contentDescription = "Icon Send")
        }
    }
}

@Composable
fun ChatLazyColumn(viewModel: ChatViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        reverseLayout = true
    ) {
        items(viewModel.messagesListState.value.size) {
            val item = viewModel.messagesListState.value.get(it)
            if (item.senderId == DataUtils.appUser?.id) {
                SentMessageRow(message = item)
            } else {
                ReceivedMessageRow(message = item)
            }

        }

    }
}

@Composable
fun ReceivedMessageRow(message: Message) {
    val date = Date(message.dateTime ?: 0)
    val simpleTimeFormat = SimpleDateFormat("hh:mm a")
    val dateString = simpleTimeFormat.format(date)
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {

        Text(
            text = message.senderName ?: "",
            style = TextStyle(color = colorResource(id = R.color.black))
        )
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = message.content ?: "",
                modifier = Modifier
                    .background(
                        colorResource(id = R.color.colorMessageBG),
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomStart = 24.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                style = TextStyle(color = colorResource(id = R.color.black), fontSize = 18.sp)
            )
            Text(
                text = dateString,
                style = TextStyle(color = colorResource(id = R.color.black)),
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
    }
}

@Composable
fun SentMessageRow(message: Message) {
    val date = Date(message.dateTime ?: 0)
    val simpleTimeFormat = SimpleDateFormat("hh:mm a")
    val dateString = simpleTimeFormat.format(date)
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {

        Text(
            text = dateString,
            style = TextStyle(color = colorResource(id = R.color.black)),
            modifier = Modifier.align(Alignment.Bottom)
        )
        Text(
            text = message.content ?: "",
            modifier = Modifier
                .background(
                    colorResource(id = R.color.blue),
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = 24.dp,
                        bottomEnd = 0.dp
                    )
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            style = TextStyle(color = colorResource(id = R.color.white), fontSize = 18.sp)
        )

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    ChatAppTheme {
        ChatScreenContent(Room(name = "Hello World"), navigator = object : Navigator {
            override fun navigateUp() {

            }
        })
    }
}