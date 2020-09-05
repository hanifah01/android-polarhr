package com.example.polar.view.hasillatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.support.KEY_DATA
import kotlinx.android.synthetic.main.activity_detail_latihan.*
import kotlinx.android.synthetic.main.activity_latihan.*
import maes.tech.intentanim.CustomIntent

class DetailLatihan : AppCompatActivity() {

    private lateinit var data : DataLatihan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_latihan)

        setupView()
    }

    private fun setupView() {
        setSupportActionBar(toolbar_detail_latihan)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Hasil Latihan")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        showData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        CustomIntent.customType(this, "right-to-left")
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CustomIntent.customType(this, "right-to-left")
    }

    private fun showData() {

        data = intent?.extras?.getParcelable(KEY_DATA)!!
        detail_dosis_latihan.text = "90%"
        detail_kualitas_latihan.text = "80%"
        detail_hrr.text = "100 bpm"
        detail_peak_hrp.text = "100 bpm"
        detail_jam_mulai.text = data.jam_mulai
        detail_jam_selesai.text = data.jam_selesai
        detail_durasi_aktif.text = data.durasi_aktif
        detail_durasi_istirahat.text = data.durasi_istirahat
        detail_durasi_total.text = data.durasi_total
    }
}
