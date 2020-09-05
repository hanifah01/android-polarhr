package com.example.polar.view.hasillatihan

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.model.Profil
import com.example.polar.support.dateDialog
import com.example.polar.support.dateDialogHasil
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.support.requestDate
import com.example.polar.support.toObject
import com.example.polar.view.Router
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_hasil_latihan.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONObject

class HasilLatihan : AppCompatActivity() {

    private val router by lazy { Router() }
    private val dialogLoading  by lazy { DialogLoading(this) }

    private var uid = ""
    private var data1 = DataLatihan()
    private var data2 = DataLatihan()
    private var data3 = DataLatihan()
    private var data4 = DataLatihan()
    private var data5 = DataLatihan()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_latihan)

        setSupportActionBar(toolbar_hasil_latihan)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Hasil Latihan")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        setupView()

    }

    private fun setupView() {
        edt_calendar.text = null
        img_calendar_latihan.setOnClickListener{
            dateDialogHasil(edt_calendar, this)
        }
        tampilData(requestDate())
        tampilkan.setOnClickListener{
            if (edt_calendar.text.toString() == ""){
                Toast.makeText(this, "Tanggal belum dipilih!", Toast.LENGTH_LONG).show()
            }else{
                tampilData(edt_calendar.text.toString())
            }
        }

        cardview1.setOnClickListener {
            router.toDetailHasil(this, data1)
        }

        cardview2.setOnClickListener {
            router.toDetailHasil(this, data2)
        }

        cardview3.setOnClickListener {
            router.toDetailHasil(this, data3)
        }

        cardview4.setOnClickListener {
            router.toDetailHasil(this, data4)
        }

        cardview5.setOnClickListener {
            router.toDetailHasil(this, data5)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
//        CustomIntent.customType(this, "right-to-left")
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun tampilData(date: String){
        dialogLoading.show(true)
        txt_resume_tanggal.text = "Resume latihan $date"
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val lat1 = db.collection(uid).document(date+"_1")
        lat1.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data1 = JSONObject(document.data).toObject(DataLatihan::class.java)
                tv_hasil_durasi1.text = data1.durasi_aktif
                lyt_cardview.visibility = View.VISIBLE
                data_kosong.visibility = View.GONE
                getData2(date)
            } else {
                lyt_cardview.visibility = View.GONE
                data_kosong.visibility = View.VISIBLE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }


    }

    private fun getData2(date: String) {
        val db = FirebaseFirestore.getInstance()
        val lat2 = db.collection(uid).document(date+"_2")
        lat2.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data2 = JSONObject(document.data).toObject(DataLatihan::class.java)
                cardview2.visibility = View.VISIBLE
                tv_hasil_durasi2.text = data2.durasi_aktif
                getData3(date)
            } else {
                cardview2.visibility = View.GONE
                cardview3.visibility = View.GONE
                cardview4.visibility = View.GONE
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    private fun getData3(date: String){
        val db = FirebaseFirestore.getInstance()
        val lat3 = db.collection(uid).document(date+"_3")
        lat3.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data3 = JSONObject(document.data).toObject(DataLatihan::class.java)
                cardview3.visibility = View.VISIBLE
                tv_hasil_durasi3.text = data3.durasi_aktif
                getData4(date)
            } else {
                cardview3.visibility = View.GONE
                cardview4.visibility = View.GONE
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    private fun getData4(date: String){
        val db = FirebaseFirestore.getInstance()
        val lat4 = db.collection(uid).document(date+"_4")
        lat4.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data4 = JSONObject(document.data).toObject(DataLatihan::class.java)
                cardview4.visibility = View.VISIBLE
                tv_hasil_durasi4.text = data4.durasi_aktif
                getData5(date)
            } else {
                cardview4.visibility = View.GONE
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    private fun getData5(date: String){
        val db = FirebaseFirestore.getInstance()
        val lat5 = db.collection(uid).document(date+"_5")
        lat5.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data5 = JSONObject(document.data).toObject(DataLatihan::class.java)
                cardview5.visibility = View.VISIBLE
                tv_hasil_durasi5.text = data5.durasi_aktif
                dialogLoading.show(false)
            } else {
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        CustomIntent.customType(this, "right-to-left")
    }
}
