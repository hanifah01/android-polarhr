package com.example.polar.view.pelatih

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.polar.R
import com.example.polar.model.Murid
import com.example.polar.model.Profil
import com.example.polar.support.PATH_PELATIH
import com.example.polar.support.PATH_PROFILE
import com.example.polar.support.TinyDB
import com.example.polar.support.toObject
import com.example.polar.view.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home_pelatih.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONObject
import kotlin.system.exitProcess

class HomePelatih : AppCompatActivity() {

    lateinit var tinydb: TinyDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pelatih)

        setupView()
    }

    private fun setupView() {
        tinydb = TinyDB(this)
        setSupportActionBar(toolbar_home_pelatih)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("")
        }

        getData()

        menu_monitoring_pelatihan.setOnClickListener {

        }
    }

    private fun getData() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(uid).document(PATH_PROFILE)
        docRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val data = JSONObject(document.data).toObject(Profil::class.java)
                coach_name.text = "Halo, pelatih ${data.nama}"
                val listAtlit = db.collection(PATH_PELATIH).document(data.nama!!)
                listAtlit.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        val data = JSONObject(document.data).toObject(Murid::class.java)

                        if (data.atlit1 != ""){
                            val docAtlit1 = db.collection(data.atlit1!!).document(PATH_PROFILE)
                            docAtlit1.get().addOnSuccessListener { document ->
                                if (document.exists()) {

                                    val dataAtlit1 = JSONObject(document.data).toObject(Profil::class.java)
                                    nama_atlit_1.text = dataAtlit1.nama

                                    if (data.atlit2 != ""){
                                        val docAtlit2 = db.collection(data.atlit2!!).document(PATH_PROFILE)
                                        docAtlit2.get().addOnSuccessListener { document ->
                                            if (document.exists()) {

                                                val dataAtlit2 = JSONObject(document.data).toObject(Profil::class.java)
                                                nama_atlit_2.text = dataAtlit2.nama

                                                if (data.atlit3 != ""){
                                                    val docAtlit3 = db.collection(data.atlit3!!).document(PATH_PROFILE)
                                                    docAtlit3.get().addOnSuccessListener { document ->
                                                        if (document.exists()) {

                                                            val dataAtlit3 = JSONObject(document.data).toObject(Profil::class.java)
                                                            nama_atlit_3.text = dataAtlit3.nama

                                                        } else {
                                                            Log.d("TAG", "No such document")
                                                        }
                                                    }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                                                }

                                            } else {
                                                Log.d("TAG", "No such document")
                                            }
                                        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                                    }

                                } else {
                                    Log.d("TAG", "No such document")
                                }
                            }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                        }

                    } else {
                        Log.d("TAG", "No such document")
                    }
                }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
            } else {
                Log.d("TAG", "No such document")
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }

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
