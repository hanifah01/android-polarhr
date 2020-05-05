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
        tinydb = TinyDB(this)
        statLogin = tinydb.getBoolean("login")
        if (statLogin as Boolean) {
            startActivity(Intent(this@Login, Home::class.java))
            CustomIntent.customType(this, "left-to-right")
        }

        // Custom font
        val typeface1 = Typeface.createFromAsset(assets, "Montserrat-Regular.ttf")
        val typeface2 = Typeface.createFromAsset(assets, "Montserrat-Bold.ttf")
        txt_login.typeface = typeface2
        txt_subjudul.typeface= typeface1

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

                        user = FirebaseAuth.getInstance().currentUser!!
                        uid = user.uid
                        Log.d("uidddddd ", uid)

                        //startActivity(Intent(this, Home::class.java))
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

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        val intent = Intent(this@Login, LandingPage::class.java)
        startActivity(intent)
        finish()
        /*if (doubleBackToExitPressedOnce) {
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
        }, 2000)*/
    }

}
