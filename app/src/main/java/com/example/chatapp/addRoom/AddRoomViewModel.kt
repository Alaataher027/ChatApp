package com.example.chatapp.addRoom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatapp.dataBase.addRoomToFirestroeDB
import com.example.chatapp.model.Category
import com.example.chatapp.model.Room


class AddRoomViewModel : ViewModel() {
    var titleState = mutableStateOf("")
    var titleErrorState = mutableStateOf("")
    var descriptionState = mutableStateOf("")
    var descriptionErrorState = mutableStateOf("")
    val categoriesList = Category.getCategoriesList()
    var selectedItem = mutableStateOf(categoriesList[0])
    var isExpanded = mutableStateOf(false)
    val showLoading = mutableStateOf(false)
    val message = mutableStateOf("")
    var navigator: Navigator? = null
    fun validateFields(): Boolean {
        if (titleState.value.isEmpty() || titleState.value.isBlank()) {
            titleErrorState.value = "Room Title Required"
            return false
        } else
            titleErrorState.value = ""
        if (descriptionState.value.isEmpty() || descriptionState.value.isBlank()) {
            descriptionErrorState.value = "Room description Required"
            return false
        } else
            descriptionErrorState.value = ""
        return true
    }

    fun addRoomToFirestore() {
        if (validateFields()) {
            val room = Room(
                null,
                name = titleState.value,
                description = descriptionState.value,
                categoryID = selectedItem.value.id ?: Category.MOVIES
            )
            showLoading.value = true
            addRoomToFirestroeDB(room, onSuccessListener = {
                showLoading.value = false
                message.value = "Room Added Successfully"
            }, onFailureListener = {
                showLoading.value = false
                message.value = "Error Occurred : ${it.localizedMessage}"

            })
        }
    }

    fun navigateUp() {
        navigator?.navigateUp()
    }
}