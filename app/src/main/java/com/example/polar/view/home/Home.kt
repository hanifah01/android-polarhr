package com.example.polar.view.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.view.latihan.PetunjukLatihan
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import maes.tech.intentanim.CustomIntent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class Home : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar_home)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("")
        }

        latihan.setOnClickListener {
            startActivity(Intent(this, PetunjukLatihan::class.java))
            CustomIntent.customType(this, "left-to-right")
        }

        database = FirebaseDatabase.getInstance().reference
        getData()
    }

    fun getData(){

        val uidRef = database.child("Samsud")
        val valueEventListener = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(p0: DataSnapshot) {
                val nama = p0.child("Nama").getValue().toString()
                val tinggi = p0.child("Height").getValue().toString()
                val berat = p0.child("Weight").getValue().toString()
                val ttl = p0.child("TglLahir").getValue().toString()
                Log.d("Hasil", nama + "\n" + tinggi + "\n" + berat + "\n" + ttl)
                atlit_name.text = "Halo, "+ nama
                tv_tb.text = tinggi + " cm"
                tv_bb.text = berat + " kg"
                calculateAge(ttl)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        uidRef.addListenerForSingleValueEvent(valueEventListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(strdate : String){
        val ttl = LocalDate.parse(strdate)
        val currentDateTime = LocalDateTime.now()
        val difference = ChronoUnit.YEARS.between(ttl, currentDateTime)
        calculateHrMax(difference)
    }

    fun calculateHrMax(age: Long){
        val hrmax = 220 - age
        tv_hrmax.text = hrmax.toString() + " bpm"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.connect_polar -> {
            Toast.makeText(this, "COnnect", Toast.LENGTH_LONG).show()
            true
        }
        R.id.logout -> {
            Toast.makeText(this, "Keluar", Toast.LENGTH_LONG).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}



