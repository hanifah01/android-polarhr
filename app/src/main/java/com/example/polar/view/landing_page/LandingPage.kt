package com.example.polar.view.landing_page

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.polar.R
import com.example.polar.view.login.Login
import com.example.polar.view.register.Register
import kotlinx.android.synthetic.main.activity_landing_page.*
import kotlinx.android.synthetic.main.activity_landing_page.btn_daftar
import kotlinx.android.synthetic.main.activity_landing_page.btn_login
import kotlinx.android.synthetic.main.activity_login.*
import maes.tech.intentanim.CustomIntent
import kotlin.system.exitProcess

class LandingPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // Custom font
        val typeface1 = Typeface.createFromAsset(assets, "Montserrat-Regular.ttf")
        txt_judul1.typeface=typeface1
        txt_judul2.typeface=typeface1
        atau.typeface= typeface1
        //btn_login.typeface=typeface1
        //btn_daftar.typeface=typeface1

        btn_daftar.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
            CustomIntent.customType(this, "left-to-right")
        }

        btn_login.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            CustomIntent.customType(this, "left-to-right")
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
