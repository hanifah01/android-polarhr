package com.example.polar.view.latihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.polar.R
import kotlinx.android.synthetic.main.activity_simpan_hasil.*

class SimpanHasil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simpan_hasil)

        setSupportActionBar(toolbar_simpanhasil)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Konfirmasi")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
