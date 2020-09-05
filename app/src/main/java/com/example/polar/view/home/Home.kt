package com.example.polar.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.model.Profil
import com.example.polar.support.PATH_PELATIH
import com.example.polar.support.PATH_PROFILE
import com.example.polar.support.TinyDB
import com.example.polar.support.toObject
import com.example.polar.view.hasillatihan.HasilLatihan
import com.example.polar.view.landingpage.LandingPage
import com.example.polar.view.latihan.PetunjukLatihan
import com.example.polar.view.login.Login
import com.example.polar.view.pelatih.HomePelatih
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.system.exitProcess

class Home : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var tinydb: TinyDB
    var statLogin: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupView()
    }

    private fun setupView() {
        tinydb = TinyDB(this)

        setSupportActionBar(toolbar_home)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("")
        }

        latihan.setOnClickListener {
            startActivity(Intent(this, PetunjukLatihan::class.java))
//            CustomIntent.customType(this, "left-to-right")
        }
        menu_hasil_latihan.setOnClickListener {
            startActivity(Intent(this, HasilLatihan::class.java))
//            CustomIntent.customType(this, "left-to-right")
        }

        database = FirebaseDatabase.getInstance().reference
        getData()

    }

    @SuppressLint("SetTextI18n")
    private fun getData(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(uid).document(PATH_PROFILE)
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val data = JSONObject(document.data).toObject(Profil::class.java)
                atlit_name.text = "Halo, ${data.nama}"
                tv_tb.text = "${data.tinggi} cm"
                tv_bb.text = "${data.berat} kg"
                tv_pelatih.text = data.nama_pelatih
                calculateAge(data.ttl)
            } else {
                Log.d("TAG", "No such document")
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    @SuppressLint("NewApi")
    fun calculateAge(strdate : String?){
        val ttl = LocalDate.parse(strdate)
        val currentDateTime = LocalDateTime.now()
        val difference = ChronoUnit.YEARS.between(ttl, currentDateTime)
        calculateHrMax(difference)
    }

    private fun calculateHrMax(age: Long){
        val hrmax = 220 - age
        tv_hrmax.text = "$hrmax bpm"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> {
            startActivity(Intent(this, Login::class.java))
            Toast.makeText(this, "Logout Berhasil", Toast.LENGTH_LONG).show()
            tinydb.putBoolean("login", false)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            moveTaskToBack(true)
            exitProcess(-1)
            finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back button again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}
