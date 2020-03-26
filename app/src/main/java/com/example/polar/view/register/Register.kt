package com.example.polar.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dev.materialspinner.MaterialSpinner
import com.example.polar.R
import com.example.polar.support.REGISTER_BERHASIL
import com.example.polar.support.REGISTER_GAGAL
import com.example.polar.support.dateDialog
import com.example.polar.view.landingpage.LandingPage
import com.example.polar.view.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import maes.tech.intentanim.CustomIntent


class Register : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var auth: FirebaseAuth

    private var list_of_items = arrayOf("Pilih jenis olahraga",
        "Lari",
        "Lari Jarak Pendek",
        "Lari Jarak Menengah",
        "Lari Jarak Jauh",
        "Lari Estafet")
    private lateinit var spinner : MaterialSpinner

    var sportValue : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        btn_lanjut.setOnClickListener{
            val namapelatih = edt_namapelatih.text.toString()
            if(namapelatih.equals("")){
                Toast.makeText(this, "Nama pelatih kosong!", Toast.LENGTH_LONG).show()
            }
            else{
                lyt_form.visibility = View.VISIBLE
                lyt_btn_register.visibility = View.VISIBLE

                lyt_namapelatih.visibility= View.GONE
                lyt_btn_pelatih.visibility= View.GONE
            }
        }
        img_calendar.setOnClickListener{
            dateDialog(edt_birthdate, this)
        }

        spinner = findViewById(R.id.material_spinner)
        spinner.getSpinner().onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)

        setupButton()
    }

    private fun setupButton() {
        btn_register.setOnClickListener {
            loading.visibility = View.VISIBLE
            val name = edt_nama.text.toString()
            val birthdate = edt_birthdate.text.toString()
            val height = edt_height.text.toString()
            val weight = edt_weight.text.toString()
            val coach = edt_namapelatih.text.toString()
            //val spinnerSport ambilnya gimana
            // lah kamu yg buat spinner masak gk tau wkwkwkwkw, belajar

            val email = edt_email.text.toString()
            val pass = edt_pass.text.toString()

            val namaDb = FirebaseDatabase.getInstance().reference.child(name).child("Atlit")
            val tglLahirDb = FirebaseDatabase.getInstance().reference.child(name).child("TglLahir")
            val heightDb = FirebaseDatabase.getInstance().reference.child(name).child("Height")
            val weightDb = FirebaseDatabase.getInstance().reference.child(name).child("Weight")
            val sportDb = FirebaseDatabase.getInstance().reference.child(name).child("Sport")
            val coachDb = FirebaseDatabase.getInstance().reference.child(name).child("Coach")

            if (pass.equals("") || email.equals("") || name.equals("") || height.equals("")||
                weight.equals("")||birthdate.equals("")||sportValue.equals("Pilih jenis olahraga")){
                Toast.makeText(this, "Field ada yang kosong!", Toast.LENGTH_LONG).show()
                loading.visibility = View.GONE
            }
            else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        namaDb.setValue(name)
                        tglLahirDb.setValue(birthdate)
                        heightDb.setValue(height)
                        weightDb.setValue(weight)
                        sportDb.setValue(sportValue)
                        coachDb.setValue(coach)

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
    }

    override  fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        val spinnerSport = arg0.getItemAtPosition(position).toString()
        sportValue = spinnerSport
        if(position == 0)
        {
            spinner.setError("Pilih jenis olahraga")
        }
        else
        {
            spinner.setErrorEnabled(false)
            spinner.setLabel("Olahraga")
        }
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {
    }

    override fun onBackPressed() {
        startActivity(Intent(this, LandingPage::class.java))
        CustomIntent.customType(this, "right-to-left")
    }

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





