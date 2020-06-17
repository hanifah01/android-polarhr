package com.example.polar.view.hasillatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.polar.R
import kotlinx.android.synthetic.main.activity_detail_latihan.*
import kotlinx.android.synthetic.main.activity_latihan.*

class DetailLatihan : AppCompatActivity() {

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

    private fun showData() {
        detail_dosis_latihan.text = "90%"
        detail_kualitas_latihan.text = "80%"
        detail_hrr.text = "100 bpm"
        detail_peak_hrp.text = "100 bpm"
        detail_jam_mulai.text = "17.00"
        detail_jam_selesai.text = "17.30"
        detail_durasi_aktif.text = "00:50"
        detail_durasi_istirahat.text = "02:00"
        detail_durasi_total.text = "02:50"
    }
}
