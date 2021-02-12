package com.example.polar.view.hasillatihan

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.support.KEY_DATA
import kotlinx.android.synthetic.main.activity_detail_latihan.*
import kotlinx.android.synthetic.main.activity_latihan.*
import maes.tech.intentanim.CustomIntent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
            supportActionBar!!.title = "Hasil Latihan"
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

    @SuppressLint("SetTextI18n")
    private fun showData() {

        data = intent?.extras?.getParcelable(KEY_DATA)!!
        detail_dosis_latihan.text = "${data.absolute_density}%"
        detail_kualitas_latihan.text = "${data.i_od}%"
        detail_hrr.text = "${data.heart_rate_latihan} bpm"
        detail_peak_hrp.text = "${data.peak_hrp} bpm"
        detail_jam_mulai.text = data.jam_mulai
        detail_jam_selesai.text = data.jam_selesai
        detail_durasi_aktif.text = "${data.durasi_aktif} menit"
        detail_durasi_istirahat.text = "${data.durasi_istirahat} menit"
        detail_durasi_total.text = "${data.durasi_total} menit"
    }

    @Suppress("DEPRECATION")
    private fun saveArrayList(arrayList: java.util.ArrayList<String>, filename: String) {
        try {
            val sdcard: File = Environment.getExternalStorageDirectory()
            val dir = File(sdcard.absolutePath + "/Iod")
            if (!dir.exists()){
                dir.mkdir()
            }
            val file = File(dir, "${filename}.txt")
            var os: FileOutputStream? = null
            os = FileOutputStream(file)
            for (i in 0 until arrayList.size){
                os.write(arrayList[i].toByteArray())
                os.write("\n".toByteArray())
            }
            os.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
