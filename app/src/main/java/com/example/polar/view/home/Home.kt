package com.example.polar.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.polar.R
import com.example.polar.view.latihan.PetunjukLatihan
import kotlinx.android.synthetic.main.activity_home.*
import maes.tech.intentanim.CustomIntent

class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_latihan.setOnClickListener {
            startActivity(Intent(this, PetunjukLatihan::class.java))
            CustomIntent.customType(this, "left-to-right")
        }
    }
}
