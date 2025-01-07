package com.example.myshop.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshop.data.MyUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val auth: FirebaseAuth = Firebase.auth
    val message = mutableStateOf("")


    fun createUserWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {

        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //LogIn
                    val displayName = task.result.user?.email?.split("@")?.get(0)
                    createUser(displayName.toString())
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        Log.d("fbError", "loginUserWithEmailAndPassword:${e.message} ")
                        message.value = "User Already Exist "
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Log.d("fbError", "loginUserWithEmailAndPassword:${e.message} ")
                        message.value = "Password Should have at least 6 characters "
                    } catch (e: Exception) {
                        Log.d("fbError", "loginUserWithEmailAndPassword:${e.message} ")

                    }
                }

            }
            _loading.value = false
        }


    }


    fun loginUserWithEmailAndPassword(email: String, password: String, goHome: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        goHome()
                    } else {
                        try {
                            throw task.exception!!
                        } catch (e: FirebaseAuthInvalidCredentialsException) {
                            Log.d("fbError", "loginUserWithEmailAndPassword:${e.message} ")
                            message.value = "Wrong Email or Password"
                        }
                    }

                }

            } catch (e: Exception) {
                Log.d("fbError", "loginUserWithEmailAndPassword: ${e.message} ")
                throw e
            }
        }

    fun signOut(onSignOutSuccess: () -> Unit, onSignOutFailure: (Exception) -> Unit) {
        try {
            auth.signOut()
            onSignOutSuccess()
        } catch (e: Exception) {
            onSignOutFailure(e)
        }
    }

    private fun createUser(displayName: String) {
        val userId = auth.currentUser?.uid.toString()
        val user =
            MyUser(id = null, userId = userId, displayName = displayName, avatarUrl = "").mapTo()
        FirebaseFirestore.getInstance().collection("users").document(auth.uid.toString())
            .set(user)

    }
}