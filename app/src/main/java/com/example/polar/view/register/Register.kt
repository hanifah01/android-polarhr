package com.example.polar.view.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.polar.R
import com.example.polar.model.Murid
import com.example.polar.model.Profil
import com.example.polar.support.*
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.view.landingpage.LandingPage
import com.example.polar.view.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import maes.tech.intentanim.CustomIntent


class Register : AppCompatActivity(){
    private val dialogLoading  by lazy { DialogLoading(this) }
    private lateinit var auth: FirebaseAuth
    lateinit var tinydb: TinyDB
    private var item_olahraga = arrayOf(
        "Pilih jenis olahraga",
        "Lari",
        "Lari Jarak Pendek",
        "Lari Jarak Menengah",
        "Lari Jarak Jauh",
        "Lari Estafet"
    )

    private var item_sebagai = arrayOf(
        "Pelatih",
        "Atlit"
    )
    private val listPelatih= ArrayList<String>()


    var sportValue : String? = null
    var userValue : String? = null
    var namaPelatih : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupView()
    }

    private fun setupView() {
        tinydb = TinyDB(this)

        auth = FirebaseAuth.getInstance()
        img_calendar.setOnClickListener{
            dateDialog(edt_birthdate, this)
            txt_edt_birthdate.setTextColor(ContextCompat.getColor(this, R.color.black))
            edt_birthdate.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        getPelatih()
        val adapterPengguna: ArrayAdapter<String> = ArrayAdapter(this, R.layout.spinner_item, item_sebagai)
        adapterPengguna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_pelatih_atlit.adapter = adapterPengguna
        spinner_pelatih_atlit.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                when(i){
                    0 -> lyt_regis_atlit.visibility = View.GONE
                    else -> lyt_regis_atlit.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        val adapterOlahraga: ArrayAdapter<String> = ArrayAdapter(this, R.layout.spinner_item, item_olahraga)
        adapterOlahraga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_olahraga.adapter = adapterOlahraga


        btn_register.setOnClickListener {
            dialogLoading.show(true)
            val name = edt_nama.text.toString()
            val birthdate = edt_birthdate.text.toString()
            val height = edt_height.text.toString()
            val weight = edt_weight.text.toString()

            val email = edt_email.text.toString()
            val pass = edt_pass.text.toString()
            sportValue = spinner_olahraga.selectedItem.toString()
            userValue = spinner_pelatih_atlit.selectedItem.toString()
            namaPelatih = spinner_nama_pelatih.selectedItem.toString()

            if (userValue == PATH_PELATIH && (pass == "" || email == "" || name == "")){
                Toast.makeText(this, getString(R.string.field_kosong), Toast.LENGTH_LONG).show()
                dialogLoading.show(false)
            }
            else if (userValue == PATH_ATLIT && (pass == "" || email == "" || name == "" || height == "" ||
                        weight == "" || birthdate == "" ||sportValue.equals(getString(R.string.pilih_jenis_olahraga))||namaPelatih.equals(getString(R.string.pilih_nama_pelatih)))){
                Toast.makeText(this, getString(R.string.field_kosong), Toast.LENGTH_LONG).show()
                dialogLoading.show(false)
            }
            else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val uid = FirebaseAuth.getInstance().currentUser!!.uid
                        val db = FirebaseFirestore.getInstance()

                        if (userValue == PATH_ATLIT){
                            val colAtlit1 = db.collection(PATH_PELATIH).document(namaPelatih!!)
                            colAtlit1.get().addOnSuccessListener { document ->
                                if (document.getString("atlit1") != null) {
                                    if (document.getString("atlit2") != null){
                                        if (document.getString("atlit3") != null){
                                            Toast.makeText(this, "Atlit sudah penuh pada pelatih ini!", Toast.LENGTH_SHORT).show()
                                            dialogLoading.show(false)
                                        }else{
                                            dialogLoading.show(false)
                                            val data: HashMap<String, Any> = HashMap()
                                            data["atlit3"] = uid
                                            val docRef = db.collection(PATH_PELATIH).document(namaPelatih!!)
                                            docRef.update(data)
                                                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                                .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                            val docRefProfil = db.collection(uid).document(PATH_PROFILE)
                                            val profile = Profil().apply {
                                                pengguna = userValue
                                                nama = name
                                                tinggi = height
                                                berat = weight
                                                ttl = birthdate
                                                jenis_olahraga = sportValue
                                                nama_pelatih = namaPelatih
                                            }
                                            docRefProfil.set(profile)
                                                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                                .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                            startActivity(Intent(this, Login::class.java))
                                            CustomIntent.customType(this, "right-to-left")
                                            Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                                            dialogLoading.show(false)

                                            tinydb.putBoolean("login", false)
                                        }
                                    }else{
                                        dialogLoading.show(false)
                                        val data: HashMap<String, Any> = HashMap()
                                        data["atlit2"] = uid
                                        val docRef = db.collection(PATH_PELATIH).document(namaPelatih!!)
                                        docRef.update(data)
                                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                        val docRefProfil = db.collection(uid).document(PATH_PROFILE)
                                        val profile = Profil().apply {
                                            pengguna = userValue
                                            nama = name
                                            tinggi = height
                                            berat = weight
                                            ttl = birthdate
                                            jenis_olahraga = sportValue
                                            nama_pelatih = namaPelatih
                                        }
                                        docRefProfil.set(profile)
                                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                        startActivity(Intent(this, Login::class.java))
                                        CustomIntent.customType(this, "right-to-left")
                                        Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                                        dialogLoading.show(false)


                                        tinydb.putBoolean("login", false)
                                    }
                                } else {
                                    val data: HashMap<String, Any> = HashMap()
                                    data["atlit1"] = uid
                                    val docRef = db.collection(PATH_PELATIH).document(namaPelatih!!)
                                    docRef.set(data)
                                        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                    val docRefProfil = db.collection(uid).document(PATH_PROFILE)
                                    val profile = Profil().apply {
                                        pengguna = userValue
                                        nama = name
                                        tinggi = height
                                        berat = weight
                                        ttl = birthdate
                                        jenis_olahraga = sportValue
                                        nama_pelatih = namaPelatih
                                    }
                                    docRefProfil.set(profile)
                                        .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                        .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                                    startActivity(Intent(this, Login::class.java))
                                    CustomIntent.customType(this, "right-to-left")
                                    Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                                    dialogLoading.show(false)
                                    tinydb.putBoolean("login", false)
                                }
                            }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
                        }
                        else{
                            val docRefProfil = db.collection(uid).document(PATH_PROFILE)
                            val profile = Profil().apply {
                                pengguna = userValue
                                nama = name
                                tinggi = height
                                berat = weight
                                ttl = birthdate
                                jenis_olahraga = sportValue
                                nama_pelatih = namaPelatih
                            }
                            docRefProfil.set(profile)
                                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                                .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
                            startActivity(Intent(this, Login::class.java))
                            CustomIntent.customType(this, "right-to-left")
                            Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                            dialogLoading.show(false)
                        }

                    } else {
                        Toast.makeText(this, REGISTER_GAGAL, Toast.LENGTH_LONG).show()
                        dialogLoading.show(false)
                    }
                }
            }
        }
    }

    private fun getPelatih(){
        dialogLoading.show(true)
        val db = FirebaseFirestore.getInstance()
        db.collection("Pelatih").get()
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    listPelatih.add("Pilih nama pelatih")
                    for (document in task.result!!) {
                        listPelatih.add(document.id)
                    }
                    val adapterPelatih: ArrayAdapter<String> = ArrayAdapter(this, R.layout.spinner_item, listPelatih)
                    adapterPelatih.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner_nama_pelatih.adapter = adapterPelatih
                    Log.d("TAG", listPelatih.toString())
                    dialogLoading.show(false)
                } else {
                    Log.d(
                        "TAG",
                        "Error getting documents: ",
                        task.exception
                    )
                    dialogLoading.show(false)
                }
            }

        spinner_nama_pelatih.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                namaPelatih = spinner_nama_pelatih.selectedItem.toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                namaPelatih = ""
            }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this, LandingPage::class.java))
        CustomIntent.customType(this, "right-to-left")
    }

}