package com.example.polar.view.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dev.materialspinner.MaterialSpinner
import com.example.polar.R
import com.example.polar.model.Profil
import com.example.polar.support.PATH_PROFILE
import com.example.polar.support.REGISTER_BERHASIL
import com.example.polar.support.REGISTER_GAGAL
import com.example.polar.support.dateDialog
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.view.landingpage.LandingPage
import com.example.polar.view.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import maes.tech.intentanim.CustomIntent


class Register : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val dialogLoading  by lazy { DialogLoading(this) }
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

        setupView()
    }

    private fun setupView() {

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

        btn_register.setOnClickListener {
            dialogLoading.showDialog(true)
            val name = edt_nama.text.toString()
            val birthdate = edt_birthdate.text.toString()
            val height = edt_height.text.toString()
            val weight = edt_weight.text.toString()
            val coach = edt_namapelatih.text.toString()

            val email = edt_email.text.toString()
            val pass = edt_pass.text.toString()

            if (pass.equals("") || email.equals("") || name.equals("") || height.equals("")||
                weight.equals("")||birthdate.equals("")||sportValue.equals("Pilih jenis olahraga")){
                Toast.makeText(this, "Field ada yang kosong!", Toast.LENGTH_LONG).show()
                dialogLoading.showDialog(false)
            }
            else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val uid = FirebaseAuth.getInstance().currentUser!!.uid
                        val db = FirebaseFirestore.getInstance()
                        val docRef = db.collection(uid).document(PATH_PROFILE)
                        val profile = Profil().apply {
                            nama = name
                            tinggi = height
                            berat = weight
                            ttl = birthdate
                            jenis_olahraga = sportValue
                        }
                        docRef.set(profile)
                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

//                        val muridnya_pelatih = hashMapOf(
//                            "murid" to uid
//                        )
//                        db.collection(coach).document("murid")
//                            .set(muridnya_pelatih)
//                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
//                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }


                        startActivity(Intent(this, Login::class.java))
                        CustomIntent.customType(this, "right-to-left")
                        Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                        dialogLoading.showDialog(false)

                    } else {
                        Toast.makeText(this, REGISTER_GAGAL, Toast.LENGTH_LONG).show()
                        dialogLoading.showDialog(false)
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