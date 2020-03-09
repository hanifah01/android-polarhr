package com.example.polar.view.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.polar.R
import com.example.polar.support.REGISTER_BERHASIL
import com.example.polar.support.REGISTER_GAGAL
import com.example.polar.view.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import maes.tech.intentanim.CustomIntent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*


class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var msg = ""

    var cal = Calendar.getInstance()
    //var edt_birthdate: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        /*btn_register.setOnClickListener {
            loading.visibility = View.VISIBLE
            daftar()
        }*/

    }

    

    /*fun daftar(){
        val email = edt_email.text.toString()
        val pass = edt_pass.text.toString()
        val name = edt_nama.text.toString()
        val current_user_db = FirebaseDatabase.getInstance().reference.child(name)
        if (pass.equals("") || email.equals("") || name.equals("")){
            Toast.makeText(this, "Field ada yang kosong!", Toast.LENGTH_LONG).show()
            loading.visibility = View.GONE
        }
        else {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful) {
                    current_user_db.setValue(name)
                    startActivity(Intent(this, Login::class.java))
                    CustomIntent.customType(this, "right-to-left")
                    Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                    loading.visibility = View.GONE
                } else {
                    Toast.makeText(this, REGISTER_GAGAL, Toast.LENGTH_LONG).show()
                    loading.visibility = View.GONE
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Login::class.java))
        CustomIntent.customType(this, "right-to-left")
    }*/
}


