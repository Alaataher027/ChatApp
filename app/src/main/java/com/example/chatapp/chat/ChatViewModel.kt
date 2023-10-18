package com.example.chatapp.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatapp.dataBase.addMessageToFirestoreDB
import com.example.chatapp.dataBase.getMessagesFromFirestoreDB
import com.example.chatapp.model.DataUtils
import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.google.firebase.firestore.DocumentChange
import java.util.Date


class ChatViewModel : ViewModel() {
    var navigator: Navigator? = null
    var room: Room? = null
    val messagesListState = mutableStateOf<List<Message>>(listOf())
    val messageFieldState = mutableStateOf("")
    fun navigateUp() {
        navigator?.navigateUp()
    }

    fun addMessageToFirestore() {
        if (messageFieldState.value.isEmpty() || messageFieldState.value.isBlank())
            return
        val message = Message(
            content = messageFieldState.value,
            dateTime = Date().time,
            senderId = DataUtils.appUser?.id,
            senderName = DataUtils.appUser?.firstName,
            roomId = room?.roomId
        )

        addMessageToFirestoreDB(
            message,
            roomId = room?.roomId!!,
            onSuccessListener = {
                messageFieldState.value = ""
            },
            onFailureListener = {
                Log.e("Tag", it.localizedMessage)
            })

    }

    fun getMessagesFromFirestore() {
        getMessagesFromFirestoreDB(roomId = room?.roomId!!, listener = { snapshots, e ->
            if (e != null) {
                Log.e("Tag", "${e.message}")
                return@getMessagesFromFirestoreDB
            }
            val mutableList = mutableListOf<Message>()
            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        mutableList.add(dc.document.toObject(Message::class.java))
                    }
//                    DocumentChange.Type.MODIFIED -> Log.d("tag", "Modified city: ${dc.document.data}")
//                    DocumentChange.Type.REMOVED -> Log.d("tag", "Removed city: ${dc.document.data}")
                    else -> {}
                }
            }
            val newList = mutableListOf<Message>()
            newList.addAll(mutableList)
            newList.addAll(messagesListState.value)

            messagesListState.value = newList.toList()
        })
    }
}
