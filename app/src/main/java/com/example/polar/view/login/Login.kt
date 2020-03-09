package com.example.polar.view.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.support.LOGIN_BERHASIL
import com.example.polar.support.LOGIN_GAGAL
import com.example.polar.view.Home
import com.example.polar.view.register.Register
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import maes.tech.intentanim.CustomIntent
import kotlin.system.exitProcess


class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Custom font
        val typeface1 = Typeface.createFromAsset(assets, "Montserrat-Regular.ttf")
        val typeface2 = Typeface.createFromAsset(assets, "Montserrat-Bold.ttf")
        txt_login.typeface = typeface2
        txt_subjudul.typeface= typeface1


        auth = FirebaseAuth.getInstance()



        /*btn_daftar.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            CustomIntent.customType(this, "left-to-right")
        }*/

        btn_login.setOnClickListener {
            loading.visibility = View.VISIBLE
            val email = edt_email.text.toString()
            val pass = edt_pass.text.toString()
            if (pass.equals("") || email.equals("")){
                Toast.makeText(this, "Email/Password Kosong!", Toast.LENGTH_LONG).show()
                loading.visibility = View.GONE
            }
            else {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, Home::class.java))
                        CustomIntent.customType(this, "left-to-right")
                        Toast.makeText(this, LOGIN_BERHASIL, Toast.LENGTH_LONG).show()
                        loading.visibility = View.GONE
                    } else {
                        Toast.makeText(this, LOGIN_GAGAL, Toast.LENGTH_LONG).show()
                        loading.visibility = View.GONE
                    }
                }
            }
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            moveTaskToBack(true)
            exitProcess(-1)
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back button again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}
