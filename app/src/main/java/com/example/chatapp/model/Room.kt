package com.example.chatapp.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Room(
    var roomId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val categoryID: String? = null
) : Parcelable {


    companion object {
        const val COLLECTION_NAME = "Room"
    }


}