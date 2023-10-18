package com.example.chatapp.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chatapp.model.DataUtils
import com.example.chatapp.dataBase.getUserFromFirestoreDB
import com.example.chatapp.model.AppUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SpalshViewModel : ViewModel() {
    var navigator: Navigator? = null
    val auth = Firebase.auth
    fun isCurrentUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun navigate() {
        if (isCurrentUserLoggedIn()) {
            // get User Data From Firestore

            getUserFromFirestoreDB(
                auth.currentUser?.uid!!,
                onSuccessListener = {
                    DataUtils.appUser = it.toObject(AppUser::class.java)
                    DataUtils.firebaseUser = auth.currentUser
                    // Navigate To Home
                    navigator?.navigateToHomeScreen()
                },
                onFailureListener = {
                    // navigate to Login
                    Log.e("Tag", it.localizedMessage)
                    navigator?.navigateToLoginScreen()
                })
        } else {
            // navigate to login Screen
            navigator?.navigateToLoginScreen()

        }

    }
}