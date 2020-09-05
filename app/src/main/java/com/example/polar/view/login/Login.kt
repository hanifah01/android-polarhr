package com.example.polar.view.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.model.Profil
import com.example.polar.support.*
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.view.home.Home
import com.example.polar.view.landingpage.LandingPage
import com.example.polar.view.pelatih.HomePelatih
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONObject


class Login : AppCompatActivity() {
    private val dialogLoading  by lazy { DialogLoading(this) }
    private lateinit var auth: FirebaseAuth
    lateinit var tinydb: TinyDB
    var statLogin: Boolean? = null
    lateinit var user : FirebaseUser
    var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupView()
    }

    private fun setupView() {
        tinydb = TinyDB(this)
        statLogin = tinydb.getBoolean("login")
        val user = tinydb.getString("user")
        if (statLogin as Boolean) {
            when(user){
                "pelatih" -> {
                    startActivity(Intent(this@Login, HomePelatih::class.java))
                    CustomIntent.customType(this, "left-to-right")
                }
                "atlit" -> {
                    startActivity(Intent(this@Login, Home::class.java))
                    CustomIntent.customType(this, "left-to-right")
                }
            }
        }

        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            dialogLoading.show(true)
            val email = edt_email.text.toString()
            val pass = edt_pass.text.toString()
            if (pass.equals("") || email.equals("")){
                Toast.makeText(this, "Email/Password Kosong!", Toast.LENGTH_LONG).show()
                dialogLoading.show(false)
            }
            else {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        statLogin = true
                        tinydb.putBoolean("login", true)

                        val uid = FirebaseAuth.getInstance().currentUser!!.uid
                        val db = FirebaseFirestore.getInstance()
                        val docRef = db.collection(uid).document(PATH_PROFILE)
                        docRef.get().addOnSuccessListener { document ->
                            if (document.exists()) {
                                val data = JSONObject(document.data).toObject(Profil::class.java)
                                if (data.pengguna == PATH_PELATIH){
                                    tinydb.putString("user", "pelatih")
                                    startActivity(Intent(this, HomePelatih::class.java))
                                    CustomIntent.customType(this, "left-to-right")
                                    dialogLoading.show(false)
                                }
                                else {
                                    tinydb.putString("user", "atlit")
                                    startActivity(Intent(this, Home::class.java))
                                    CustomIntent.customType(this, "left-to-right")
                                    dialogLoading.show(false)
                                }
                            } else {
                                Log.d("TAG", "No such document")
                            }
                        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }

                        Toast.makeText(this, LOGIN_BERHASIL, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, LOGIN_GAGAL, Toast.LENGTH_LONG).show()
                        dialogLoading.show(false)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, LandingPage::class.java))
        CustomIntent.customType(this, "right-to-left")
    }

}
