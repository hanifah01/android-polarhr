package com.example.polar.view.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.support.LOGIN_BERHASIL
import com.example.polar.support.LOGIN_GAGAL
import com.example.polar.support.TinyDB
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.view.home.Home
import com.example.polar.view.landingpage.LandingPage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import maes.tech.intentanim.CustomIntent


class Login : AppCompatActivity() {
    private val dialogLoading  by lazy { DialogLoading(this) }
    private lateinit var auth: FirebaseAuth
    lateinit var tinydb: TinyDB
    var statLogin: Boolean? = null
    lateinit var user : FirebaseUser
    var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupView()
    }

    private fun setupView() {
        tinydb = TinyDB(this)
        statLogin = tinydb.getBoolean("login")
        if (statLogin as Boolean) {
            startActivity(Intent(this@Login, Home::class.java))
            CustomIntent.customType(this, "left-to-right")
        }

        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            dialogLoading.showDialog(true)
            val email = edt_email.text.toString()
            val pass = edt_pass.text.toString()
            if (pass.equals("") || email.equals("")){
                Toast.makeText(this, "Email/Password Kosong!", Toast.LENGTH_LONG).show()
                dialogLoading.showDialog(false)
            }
            else {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        statLogin = true
                        tinydb.putBoolean("login", true)

                        startActivity(Intent(this, Home::class.java))
                        CustomIntent.customType(this, "left-to-right")
                        Toast.makeText(this, LOGIN_BERHASIL, Toast.LENGTH_LONG).show()
                        dialogLoading.showDialog(false)
                    } else {
                        Toast.makeText(this, LOGIN_GAGAL, Toast.LENGTH_LONG).show()
                        dialogLoading.showDialog(false)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, LandingPage::class.java))
        CustomIntent.customType(this, "right-to-left")
    }

}
