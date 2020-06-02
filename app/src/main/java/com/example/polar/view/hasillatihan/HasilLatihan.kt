package com.example.polar.view.hasillatihan

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_hasil_latihan.*
import org.json.JSONObject

class HasilLatihan : AppCompatActivity() {

    private val dialogLoading  by lazy { DialogLoading(this) }
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
            if (edt_calendar.text.toString().equals("")){
                Toast.makeText(this, "Tanggal belum dipilih!", Toast.LENGTH_LONG).show()
            }else{
                tampilData(edt_calendar.text.toString())
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun tampilData(date: String){
        dialogLoading.showDialog(true)
        txt_resume_tanggal.text = "Resume latihan " +  date
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(uid).document(date+"_1")
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val data = JSONObject(document.data).toObject(DataLatihan::class.java)
                lyt_cardview.visibility = View.VISIBLE
                data_kosong.visibility = View.GONE
                dialogLoading.showDialog(false)
            } else {
                lyt_cardview.visibility = View.GONE
                data_kosong.visibility = View.VISIBLE
                dialogLoading.showDialog(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }
}
