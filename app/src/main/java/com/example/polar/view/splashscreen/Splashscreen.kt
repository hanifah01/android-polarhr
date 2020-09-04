package com.example.polar.view.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.polar.R
import com.example.polar.view.landingpage.LandingPage

class Splashscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        startActivity(Intent(this, LandingPage::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }).also { finish() }
    }
}
