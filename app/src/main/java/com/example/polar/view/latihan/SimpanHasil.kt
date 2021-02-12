package com.example.polar.view.latihan

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.support.KEY_DATA
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.support.requestDate
import com.example.polar.view.home.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_simpan_hasil.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat


class SimpanHasil : AppCompatActivity() {

    private val dialogLoading  by lazy { DialogLoading(this) }

    private lateinit var data : DataLatihan
    var arrayHrData = ArrayList<String>()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simpan_hasil)

        arrayHrData = intent?.extras?.getStringArrayList("array")!!
        data = intent?.extras?.getParcelable(KEY_DATA)!!
        tv_tgl_latihan.text = requestDate()
        tv_jam_latihan.text = data.jam_mulai + " - " + data.jam_selesai
        tv_durasi_aktif.text = data.durasi_aktif
        tv_durasi_istirahat.text = data.durasi_istirahat
        tv_total_durasi.text = data.durasi_total
        tv_peak_hrp.text = data.peak_hrp
        tv_hrr.text = data.heart_rate_latihan
        val minIntes = (data.heart_rate_reserve!!.toFloat() / data.heart_rate_max!!.toFloat())*100
        val maxIntes = (data.heart_rate_latihan!!.toFloat() / data.heart_rate_max!!.toFloat())*100
        tv_intensitas.text = "min ${minIntes}% - max ${maxIntes}%"

        val waktuAktif = getMinute(data.durasi_aktif!!)
        val waktuIstirahat = getMinute(data.durasi_istirahat!!)
        val waktuTotal = getMinute(data.durasi_total!!)

        val hrrMax = data.heart_rate_max?.toFloat()
        val hrLat = data.heart_rate_latihan?.toFloat()
        val partialIntensity = (hrLat!!/ hrrMax!!) * 100
        val dnVl = partialIntensity*waktuAktif.toInt()

        val absoluteDensity = (waktuAktif.toFloat()-waktuIstirahat.toFloat())/(waktuAktif.toFloat()*100)
        val overalIntensity = dnVl/waktuAktif.toFloat()
        val iod = overalIntensity*absoluteDensity*waktuAktif.toFloat()/10000


        tv_dosis_pelatihan.text = iod.toString()
        tv_parsial_intensitas.text = overalIntensity.toString()
        tv_kualitas_pelatihan.text = absoluteDensity.toString()

        data.apply {
            durasi_aktif = waktuAktif
            durasi_istirahat = waktuIstirahat
            durasi_total = waktuTotal
            partial_intensity = partialIntensity.toString()
            dn_vl = dnVl.toString()
            absolute_density = absoluteDensity.toString()
            overal_intensity = overalIntensity.toString()
            i_od = tv_dosis_pelatihan.text.toString()
            tanggal_latihan = tv_tgl_latihan.text.toString()
        }

        Log.d("oke", Gson().toJson(data))
        setSupportActionBar(toolbar_simpanhasil)
        if (supportActionBar != null) {
            supportActionBar!!.title = "Konfirmasi"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }


        btn_simpan_hasil.setOnClickListener {
            if (tv_dosis_pelatihan.text.toString() == "NaN" || tv_parsial_intensitas.text.toString() == "NaN" || tv_kualitas_pelatihan.text.toString() == "NaN"){
                Toast.makeText(this, "Data tidak valid, tidak bisa disimpan", Toast.LENGTH_SHORT).show()
            }else{
                simpanData()
            }
        }

    }
    private fun simpanData(){
        dialogLoading.show(true)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(uid).document(requestDate() + "_1")
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val docRef = db.collection(uid).document(requestDate() + "_2")
                docRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val docRef = db.collection(uid).document(requestDate() + "_3")
                        docRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                val docRef = db.collection(uid).document(requestDate() + "_4")
                                docRef.get().addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val docRef = db.collection(uid).document(requestDate() + "_5")
                                        docRef.get().addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                showDialog()
                                                dialogLoading.show(false)
                                            } else {
                                                saveArrayList(arrayHrData, requestDate() + "_5")
                                                docRef.set(data)
                                                    .addOnSuccessListener { Log.d(
                                                        "TAG",
                                                        "DocumentSnapshot successfully written!"
                                                    ) }
                                                    .addOnFailureListener { e -> Log.w(
                                                        "TAG",
                                                        "Error writing document",
                                                        e
                                                    ) }
                                                dialogLoading.show(false)
                                                startActivity(Intent(this, Home::class.java))
                                            }
                                        }.addOnFailureListener { exception -> Log.e(
                                            "TAG",
                                            "get failed with ",
                                            exception
                                        ) }
                                    } else {
                                        saveArrayList(arrayHrData, requestDate() + "_4")
                                        docRef.set(data)
                                            .addOnSuccessListener { Log.d(
                                                "TAG",
                                                "DocumentSnapshot successfully written!"
                                            ) }
                                            .addOnFailureListener { e -> Log.w(
                                                "TAG",
                                                "Error writing document",
                                                e
                                            ) }
                                        dialogLoading.show(false)
                                        startActivity(Intent(this, Home::class.java))
                                    }
                                }.addOnFailureListener { exception -> Log.e(
                                    "TAG",
                                    "get failed with ",
                                    exception
                                ) }
                            } else {
                                saveArrayList(arrayHrData, requestDate() + "_3")
                                docRef.set(data)
                                    .addOnSuccessListener { Log.d(
                                        "TAG",
                                        "DocumentSnapshot successfully written!"
                                    ) }
                                    .addOnFailureListener { e -> Log.w(
                                        "TAG",
                                        "Error writing document",
                                        e
                                    ) }
                                dialogLoading.show(false)
                                startActivity(Intent(this, Home::class.java))
                            }
                        }.addOnFailureListener { exception -> Log.e(
                            "TAG",
                            "get failed with ",
                            exception
                        ) }
                    } else {
                        saveArrayList(arrayHrData, requestDate() + "_2")
                        docRef.set(data)
                            .addOnSuccessListener { Log.d(
                                "TAG",
                                "DocumentSnapshot successfully written!"
                            ) }
                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                        dialogLoading.show(false)
                        startActivity(Intent(this, Home::class.java))
                    }
                }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
            } else {
                saveArrayList(arrayHrData, requestDate() + "_1")
                docRef.set(data)
                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                dialogLoading.show(false)
                startActivity(Intent(this, Home::class.java))
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        val yesBtn = dialog.findViewById(R.id.btn_yes) as TextView
        yesBtn.setOnClickListener {
            //dialog.dismiss()
            startActivity(Intent(this, Home::class.java))
        }
        dialog.show()

    }

    fun getMinute(tim: String):String{
        val sdf = SimpleDateFormat("HH:mm:ss") // or "hh:mm" for 12 hour format
        val time = sdf.parse(tim)
        return time.minutes.toString()
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
