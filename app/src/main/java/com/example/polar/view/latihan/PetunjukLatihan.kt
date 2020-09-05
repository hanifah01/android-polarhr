package com.example.polar.view.latihan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.polar.R
import com.example.polar.view.home.Home
import kotlinx.android.synthetic.main.activity_petunjuk_latihan.*
import maes.tech.intentanim.CustomIntent

class PetunjukLatihan : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petunjuk_latihan)

        setSupportActionBar(toolbar_petunjuklatihan)
        if (supportActionBar != null) {
            supportActionBar!!.title = "Latihan"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        btn_petunjuklanjut.setOnClickListener{
            startActivity(Intent(this, PetunjukPemanasan::class.java))
            CustomIntent.customType(this, "left-to-right")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
