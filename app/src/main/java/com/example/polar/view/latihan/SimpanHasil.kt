package com.example.polar.view.latihan

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.support.KEY_DATA
import com.example.polar.support.PATH_LATIHAN
import com.example.polar.support.PATH_PROFILE
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.support.requestDate
import com.example.polar.view.hasillatihan.HasilLatihan
import com.example.polar.view.home.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_simpan_hasil.*
import maes.tech.intentanim.CustomIntent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SimpanHasil : AppCompatActivity() {

    private val dialogLoading  by lazy { DialogLoading(this) }

    private lateinit var data : DataLatihan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simpan_hasil)
        data = intent?.extras?.getParcelable(KEY_DATA)!!
        tv_jam_latihan.text = data.jam_mulai + " - " + data.jam_selesai
        tv_durasi_aktif.text = data.durasi_aktif
        tv_durasi_istirahat.text = data.durasi_istirahat
        tv_total_durasi.text = data.durasi_total
        setSupportActionBar(toolbar_simpanhasil)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Konfirmasi")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }


        btn_simpan_hasil.setOnClickListener {
            simpanData()
        }
    }
    fun simpanData(){
        dialogLoading.show(true)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(uid).document(requestDate()+"_1")
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val docRef = db.collection(uid).document(requestDate()+"_2")
                docRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val docRef = db.collection(uid).document(requestDate()+"_3")
                        docRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                val docRef = db.collection(uid).document(requestDate()+"_4")
                                docRef.get().addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val docRef = db.collection(uid).document(requestDate()+"_5")
                                        docRef.get().addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                showDialog()
                                                dialogLoading.show(false)
                                                //Toast.makeText(this, "Latihan maksimal 5x sehari!", Toast.LENGTH_LONG).show()
                                            } else {
                                                docRef.set(data)
                                                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                                    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                                dialogLoading.show(false)
                                                startActivity(Intent(this, Home::class.java))
                                            }
                                        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                                    } else {
                                        docRef.set(data)
                                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                        dialogLoading.show(false)
                                        startActivity(Intent(this, Home::class.java))
                                    }
                                }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                            } else {
                                docRef.set(data)
                                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                dialogLoading.show(false)
                                startActivity(Intent(this, Home::class.java))
                            }
                        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                    } else {
                        docRef.set(data)
                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                        dialogLoading.show(false)
                        startActivity(Intent(this, Home::class.java))
                    }
                }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
            } else {
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
}
