package com.example.polar.view.hasillatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.support.adapter.HasilLatihanAdapter
import com.example.polar.support.dateDialog
import com.example.polar.support.requestDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_hasil_latihan.*

class HasilLatihan : AppCompatActivity() {
    private lateinit var data : DataLatihan

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
            dateDialog(edt_calendar, this)
        }

        tampilkan.setOnClickListener(View.OnClickListener {
            tampilData()
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun tampilData(){
        //val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val uid =  "wvFDqnsg0ncgAJ6ILDg7Sxz6ncL2"
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(uid).document("Rab-05-2020"+"_1")
        docRef.get().addOnSuccessListener { document ->
            tv_hasil_durasi1.text = document.data?.get("durasi_total").toString()
            tv_hasil_kualitas1.text = document.data?.get("kualitas_pelatihan").toString()
            if (document.exists()) {
                val docRef = db.collection(uid).document("Rab-05-2020"+"_2")
                docRef.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val docRef = db.collection(uid).document("Rab-05-2020"+"_3")
                        docRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                val docRef = db.collection(uid).document("Rab-05-2020"+"_4")
                                docRef.get().addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val docRef = db.collection(uid).document("Rab-05-2020"+"_5")
                                        docRef.get().addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                Toast.makeText(this, "Latihan maksimal 5x sehari!", Toast.LENGTH_LONG).show()
                                            } else {
                                                docRef.set(data)
                                                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                                    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                            }
                                        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                                    } else {
                                        docRef.set(data)
                                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                    }
                                }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                            } else {
                                docRef.set(data)
                                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                            }
                        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                    } else {
                        docRef.set(data)
                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                    }
                }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
            } else {
                docRef.set(data)
                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }
}
