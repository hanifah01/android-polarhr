package com.example.polar.view.register

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.isVisible
import com.dev.materialspinner.MaterialSpinner
import com.example.polar.R
import com.example.polar.support.REGISTER_BERHASIL
import com.example.polar.support.REGISTER_GAGAL
import com.example.polar.view.login.Login
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import maes.tech.intentanim.CustomIntent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*


class Register : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var auth: FirebaseAuth

    private var list_of_items = arrayOf("Select Sport","USA", "Japan", "India")
    private lateinit var spinner : MaterialSpinner



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()


        btn_register.setOnClickListener {
            loading.visibility = View.VISIBLE
            daftar()
        }

        img_calendar.setOnClickListener{
            getCalendar()
        }

        spinner = findViewById(R.id.material_spinner)
        spinner.getSpinner().onItemSelectedListener = this

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.setAdapter(aa)


    }


    fun getCalendar(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in TextView
            edt_birthdate.setText("" + dayOfMonth + " " + month + ", " + year)
        }, year, month, day)
        dpd.show()

    }

     override  fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {

         val spinnerSport = arg0.getItemAtPosition(position).toString()

         // use position to know the selected item
        if(position == 0)
        {
            spinner.setError("Please select Country")
        }
        else
        {
            spinner.setErrorEnabled(false)
            spinner.setLabel("Sport")
        }
    }

     override fun onNothingSelected(arg0: AdapterView<*>) {

    }

    fun daftar(){
        val name = edt_nama.text.toString()
        val birthdate = edt_birthdate.text.toString()
        val height = edt_height.text.toString()
        val weight = edt_weight.text.toString()

        //val spinnerSport ambilnya gimana

        val email = edt_email.text.toString()
        val pass = edt_pass.text.toString()

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





