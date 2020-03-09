package com.example.polar.view.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.polar.MainActivity
import com.example.polar.R
import com.example.polar.view.login.Login

class Splashscreen : AppCompatActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@Splashscreen, Login::class.java)
            startActivity(intent)
            finish()
        }, 5000)
    }
}
