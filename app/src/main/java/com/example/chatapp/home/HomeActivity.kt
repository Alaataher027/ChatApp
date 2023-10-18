package com.example.chatapp.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapp.R
import com.example.chatapp.home.ui.theme.ChatAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.addRoom.AddRoomActivity
import com.example.chatapp.chat.ChatActivity
import com.example.chatapp.model.Category
import com.example.chatapp.model.Constants
import com.example.chatapp.model.Room

class HomeActivity : ComponentActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatAppTheme {
                // A surface container using the 'background' color from the theme
                HomeContent(navigator = this)
            }
        }
    }

    override fun navigateToAddRoomScreen() {
        val intent = Intent(this@HomeActivity, AddRoomActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToChatScreen(room: Room) {
        val intent = Intent(this@HomeActivity, ChatActivity::class.java)
        intent.putExtra(Constants.EXTRA_ROOM, room)
        startActivity(intent)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(viewModel: HomeViewModel = viewModel(), navigator: Navigator) {
    viewModel.navigator = navigator
    Scaffold(
        topBar = {
            Text(
                text = stringResource(id = R.string.home),
                style = TextStyle(
                    color = colorResource(
                        id = R.color.white
                    ),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    viewModel.navigateToAddRoomScreen()
                },
                contentColor = colorResource(id = R.color.white),
                containerColor = colorResource(id = R.color.blue)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = " ")
            }
        }) {

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds // fitXY
            )
            .padding(vertical = 100.dp)
    ) {
        ChatRoomsLazyGrid(navigator = navigator)
    }


}

@Composable
fun ChatRoomsLazyGrid(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigator: Navigator,
) {
    viewModel.navigator = navigator
    viewModel.getRoomsFromFirestore()
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(viewModel.roomsListState.value.size) { position ->
            chatRoomCard(
                room = viewModel.roomsListState.value.get(position),
                navigator = navigator
            )

        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun chatRoomCard(
    room: Room,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigator: Navigator
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            viewModel.navigateToChatScreen(room = room)

        },
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 20.dp)

    ) {
        Image(
            painter = painterResource(
                id = Category.fromId(room.categoryID ?: Category.MUSIC).imageId ?: R.drawable.music
            ),
            contentDescription = "Room category Image",
            modifier = Modifier
                .height(100.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillHeight
        )
        Text(
            text = room.name ?: "",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp),
            style = TextStyle(textAlign = TextAlign.Center)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview3() {
    ChatAppTheme {
        HomeContent(navigator = object : Navigator {
            override fun navigateToAddRoomScreen() {
            }

            override fun navigateToChatScreen(room: Room) {
            }

        })

    }
}