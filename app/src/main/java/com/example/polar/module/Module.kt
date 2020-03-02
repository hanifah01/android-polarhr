package com.example.polar.module

import android.util.Log
import android.widget.Toast
import com.example.polar.R
import com.example.polar.support.LOGIN_BERHASIL
import com.example.polar.support.LOGIN_GAGAL
import com.example.polar.support.REGISTER_BERHASIL
import com.example.polar.support.REGISTER_GAGAL
import com.google.firebase.auth.FirebaseAuth

var msg = ""

fun login(auth: FirebaseAuth, email: String, pass: String): String{
    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
        if (it.isSuccessful) {
            msg = LOGIN_BERHASIL
        } else {
            msg = LOGIN_GAGAL
        }
    }
    return msg
}

fun register(auth: FirebaseAuth, email: String, pass: String): String{
    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
        if (it.isSuccessful) {
            msg = REGISTER_BERHASIL
        } else {
            msg = REGISTER_GAGAL
        }
    }
    return msg
}



