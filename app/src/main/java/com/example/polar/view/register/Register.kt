package com.example.polar.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.polar.R
import com.example.polar.module.msg
import com.example.polar.module.register
import com.example.polar.support.REGISTER_BERHASIL
import com.example.polar.support.REGISTER_GAGAL
import com.example.polar.view.login.Login
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.edt_email
import kotlinx.android.synthetic.main.activity_register.edt_pass
import maes.tech.intentanim.CustomIntent

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        btn_register.setOnClickListener {
            auth.createUserWithEmailAndPassword(edt_email.text.toString(), edt_pass.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, Login::class.java))
                    CustomIntent.customType(this, "right-to-left")
                    Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, REGISTER_GAGAL, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, Login::class.java))
        CustomIntent.customType(this, "right-to-left")
    }
}
