package com.example.chatapp.dataBase

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.chatapp.model.AppUser
import com.example.chatapp.model.Message
import com.example.chatapp.model.Room

fun getCollectionRef(collectionName: String): CollectionReference {
    val db = Firebase.firestore
    return db.collection(collectionName)
}

fun addUserToFirestoreDB(
    appUser: AppUser,
    onSuccessListener: OnSuccessListener<Void>,
    onFailureListener: OnFailureListener
) {


    getCollectionRef(AppUser.COLLECTION_NAME)
        .document(appUser.id!!)
        .set(appUser)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)
}

fun getUserFromFirestoreDB(
    uid: String,
    onSuccessListener: OnSuccessListener<DocumentSnapshot>,
    onFailureListener: OnFailureListener
) {
    getCollectionRef(AppUser.COLLECTION_NAME)
        .document(uid)
        .get()
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)

}

fun addRoomToFirestroeDB(
    room: Room,
    onSuccessListener: OnSuccessListener<Void>,
    onFailureListener: OnFailureListener,
) {
    val documentReference = getCollectionRef(Room.COLLECTION_NAME).document()
    room.roomId = documentReference.id
    documentReference
        .set(room)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)
}

fun getRoomsFromFirestoreDB(
    onSuccessListener: OnSuccessListener<QuerySnapshot>,
    onFailureListener: OnFailureListener
) {
    val roomsCollection = getCollectionRef(Room.COLLECTION_NAME)
    roomsCollection.get()
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)
}

fun getMessagesRef(roomId: String): CollectionReference {
    val roomCollectionRef = getCollectionRef(Room.COLLECTION_NAME)
    val roomDoc = roomCollectionRef.document(roomId)
    return roomDoc.collection(Message.COLLECTION_NAME)

}

fun addMessageToFirestoreDB(
    message: Message,
    roomId: String,
    onSuccessListener: OnSuccessListener<Void>,
    onFailureListener: OnFailureListener
) {
    val messageCollection = getMessagesRef(roomId)
    val messageDoc = messageCollection.document()
    message.id = messageDoc.id
    messageDoc.set(message)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)

}

fun getMessagesFromFirestoreDB(roomId: String, listener: EventListener<QuerySnapshot>) {
    val messageCollection = getMessagesRef(roomId = roomId)
    messageCollection
        .orderBy("dateTime", Query.Direction.DESCENDING)
        .addSnapshotListener(listener)
}