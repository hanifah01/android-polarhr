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
import androidx.core.view.isGone
import androidx.core.view.isVisible
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
import org.json.JSONObject
import java.util.List


class Register : AppCompatActivity(){
    private val dialogLoading  by lazy { DialogLoading(this) }
    private lateinit var auth: FirebaseAuth
    private var item_olahraga = arrayOf(
        "Lari",
        "Lari Jarak Pendek",
        "Lari Jarak Menengah",
        "Lari Jarak Jauh",
        "Lari Estafet",
        "Pilih jenis olahraga"
    )

    private var item_sebagai = arrayOf(
        "Pelatih",
        "Atlit",
        "Pilih pengguna"
    )


    var sportValue : String? = null
    var userValue : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupView()
    }

    private fun setupView() {

        auth = FirebaseAuth.getInstance()
        img_calendar.setOnClickListener{
            dateDialog(edt_birthdate, this)
            txt_edt_birthdate.setTextColor(ContextCompat.getColor(this, R.color.black))
            edt_birthdate.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        val adapterPengguna = object : ArrayAdapter<String?>(this, android.R.layout.simple_spinner_dropdown_item, item_sebagai) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                    (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count) //"Hint to be displayed"
                }
                return v
            }
            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        adapterPengguna.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_pelatih_atlit.adapter = adapterPengguna
        spinner_pelatih_atlit.setSelection(adapterPengguna.count)
        spinner_pelatih_atlit.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                when(i){
                    0 -> lyt_regis_atlit.visibility = View.GONE
                    else -> lyt_regis_atlit.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        val adapterOlahraga = object : ArrayAdapter<String?>(this, android.R.layout.simple_spinner_dropdown_item, item_olahraga) {
            override fun getView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getView(position, convertView, parent)
                if (position == count) {
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                    (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count) //"Hint to be displayed"
                }
                return v
            }
            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        adapterOlahraga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_olahraga.adapter = adapterOlahraga
        spinner_olahraga.setSelection(adapterOlahraga.count)


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

            if (userValue == PATH_PELATIH && (pass.equals("") || email.equals("") || name.equals(""))){
                Toast.makeText(this, getString(R.string.field_kosong), Toast.LENGTH_LONG).show()
                dialogLoading.show(false)
            }
            else if (userValue == PATH_ATLIT && (pass.equals("") || email.equals("") || name.equals("") || height.equals("")||
                weight.equals("")||birthdate.equals("")||sportValue.equals(getString(R.string.pilih_jenis_olahraga)))){
                Toast.makeText(this, getString(R.string.field_kosong), Toast.LENGTH_LONG).show()
                dialogLoading.show(false)
            }
            else {
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {

                        val uid = FirebaseAuth.getInstance().currentUser!!.uid
                        val db = FirebaseFirestore.getInstance()
                        val docRef = db.collection(uid).document(PATH_PROFILE)
                        val profile = Profil().apply {
                            pengguna = userValue
                            nama = name
                            tinggi = height
                            berat = weight
                            ttl = birthdate
                            jenis_olahraga = sportValue
                        }
                        docRef.set(profile)
                            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

                        val murid = Murid()
                        val docRefPelatih = db.collection(PATH_PELATIH).document(name)
                        if (userValue == PATH_PELATIH){
                            docRefPelatih.set(murid)
                                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully updated!") }
                                .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }
                        }

                        startActivity(Intent(this, Login::class.java))
                        CustomIntent.customType(this, "right-to-left")
                        Toast.makeText(this, REGISTER_BERHASIL, Toast.LENGTH_LONG).show()
                        dialogLoading.show(false)

                    } else {
                        Toast.makeText(this, REGISTER_GAGAL, Toast.LENGTH_LONG).show()
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