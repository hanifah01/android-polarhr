package com.example.polar.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.model.Profil
import com.example.polar.support.PATH_PROFILE
import com.example.polar.support.TinyDB
import com.example.polar.support.requestDate2
import com.example.polar.support.toObject
import com.example.polar.view.hasillatihan.HasilLatihan
import com.example.polar.view.latihan.PetunjukLatihan
import com.example.polar.view.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

@Suppress("DEPRECATION")
class Home : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var tinydb: TinyDB
    var arrayHrData = ArrayList<String>()

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
        arrayHrData.add("oaksdoas")
        arrayHrData.add("tahu")
        arrayHrData.add("makan")

        latihan.setOnClickListener {
            startActivity(Intent(this, PetunjukLatihan::class.java).putExtra("hrr", tv_hrmax.text.toString()))
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
//        val ttl = LocalDate.parse(strdate)
//        val currentDateTime = LocalDateTime.now()
//        val difference = ChronoUnit.YEARS.between(ttl, currentDateTime)

        var bedaHari: Long = 0
        val dt: Date
        val dt2: Date
        try {
            dt = SimpleDateFormat("yyyy-MM-dd").parse(strdate)
            dt2 = SimpleDateFormat("yyyy-MM-dd").parse(requestDate2())
            bedaHari = dt2.getTime() - dt.getTime()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var m = TimeUnit.MILLISECONDS.toDays(bedaHari)
        val year: Long
        year = m / 365
        calculateHrMax(year)
    }

    private fun calculateHrMax(age: Long){
        val hrmax = 220 - age
        tv_hrmax.text = "$hrmax"
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
