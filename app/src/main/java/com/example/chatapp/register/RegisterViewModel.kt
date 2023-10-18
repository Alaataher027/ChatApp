package com.example.chatapp.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatapp.dataBase.addUserToFirestoreDB
import com.example.chatapp.dataBase.getUserFromFirestoreDB
import com.example.chatapp.model.AppUser
import com.example.chatapp.model.DataUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    val firstNameState = mutableStateOf("")
    val firstNameError = mutableStateOf("")
    val emailState = mutableStateOf("")
    val emailError = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val passwordError = mutableStateOf("")
    val showLoading = mutableStateOf<Boolean>(false)
    val message = mutableStateOf("")
    val auth = Firebase.auth
    var navigator: Navigator? = null

    fun getUserFromFirestore() {
        getUserFromFirestoreDB(
            auth.currentUser?.uid!!,
            onSuccessListener = {
                DataUtils.appUser = it.toObject(AppUser::class.java)
                DataUtils.firebaseUser = auth.currentUser

            },
            onFailureListener = {
                // navigate to Login
                Log.e("Tag", "${it.localizedMessage}")
            })
    }
    fun validateFields(): Boolean {
        if (firstNameState.value.isEmpty() || firstNameState.value.isBlank()) {
            firstNameError.value = "First name requirde"
            return false
        } else {
            firstNameError.value = ""
        }
        if (emailState.value.isEmpty() || emailState.value.isBlank()) {
            emailError.value = "Email requirde"
            return false
        } else {
            emailError.value = ""
        }
        if (passwordState.value.isEmpty() || passwordState.value.isBlank()) {
            passwordError.value = "Password requirde"
            return false
        } else {
            passwordError.value = ""
        }
        return true
    }


    fun sendAuthDataToFirebase() {
        if (validateFields()) {
            showLoading.value = true
            // register user to firebase Auth

            registerUserToAuth()

        }
    }

    fun registerUserToAuth() {
        auth.createUserWithEmailAndPassword(emailState.value, passwordState.value)
            .addOnCompleteListener {
                showLoading.value = false
                if (it.isSuccessful) {
                    // navigate to home screen

                    // add user to firebase:
                    addUserToFireStore(it.result.user?.uid)

                } else {
                    message.value = it.exception?.localizedMessage ?: ""
                    Log.e("TAG", "register Auth: ${it.exception?.localizedMessage}")

                }
            }
    }

    fun addUserToFireStore(uid: String?) {
        showLoading.value = true
        addUserToFirestoreDB(
            AppUser(
                id = uid,
                firstName = firstNameState.value,
                email = emailState.value
            ),
            onSuccessListener = {
                //message.value = "Successful Registration"
                showLoading.value = false
                getUserFromFirestore()
                navigator?.navigateToHome()
            },
            onFailureListener = {
                showLoading.value = false
                message.value = it.localizedMessage ?: ""
            }
        )

    }
}