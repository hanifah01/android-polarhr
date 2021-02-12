package com.example.polar.view.pelatih

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.support.KEY_DATA
import com.example.polar.support.dateDialogHasil
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.support.requestDate
import com.example.polar.support.toObject
import com.example.polar.view.Router
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_hasil_latihan_pelatih.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HasilLatihanPelatih : AppCompatActivity() {

    private val router by lazy { Router() }
    private val dialogLoading  by lazy { DialogLoading(this) }

    private var uid = ""
    private var data1 = DataLatihan()
    private var data2 = DataLatihan()
    private var data3 = DataLatihan()
    private var data4 = DataLatihan()
    private var data5 = DataLatihan()
    var dataList = ArrayList<DataLatihan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hasil_latihan_pelatih)
        setSupportActionBar(toolbar_hasil_latihan)
        if (supportActionBar != null) {
            supportActionBar!!.title = "Hasil Latihan"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        uid = intent?.extras?.getString(KEY_DATA)!!
        setupView()
    }

    private fun setupView() {
        edt_calendar.text = null
        img_calendar_latihan.setOnClickListener{
            dateDialogHasil(edt_calendar, this)
        }
        dataList.clear()
        tampilData(requestDate())
        tampilkan.setOnClickListener{
            if (edt_calendar.text.toString() == ""){
                Toast.makeText(this, "Tanggal belum dipilih!", Toast.LENGTH_LONG).show()
            }else{
                dataList.clear()
                tampilData(edt_calendar.text.toString())

            }
        }


        cardview1.setOnClickListener {
            router.toDetailHasil(this, data1)
        }

        cardview2.setOnClickListener {
            router.toDetailHasil(this, data2)
        }

        cardview3.setOnClickListener {
            router.toDetailHasil(this, data3)
        }

        cardview4.setOnClickListener {
            router.toDetailHasil(this, data4)
        }

        cardview5.setOnClickListener {
            router.toDetailHasil(this, data5)
        }
        img_save.setOnClickListener {
            saveData("Resume, ${edt_calendar.text}")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
//        CustomIntent.customType(this, "right-to-left")
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun tampilData(date: String){
        dialogLoading.show(true)
        txt_resume_tanggal.text = "Resume latihan $date"
        val db = FirebaseFirestore.getInstance()
        val lat1 = db.collection(uid).document(date+"_1")
        lat1.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data1 = JSONObject(document.data).toObject(DataLatihan::class.java)
                dataList.add(data1)
                getTotal(dataList)
                Log.d("cek", data1.toString())
                tv_hasil_durasi1.text = "${data1.durasi_aktif} menit"
                if (data1.i_od == "NaN"){
                    tv_hasil_dosis1.text = "${data1.i_od}%"
                }else{
                    val iod = data1.i_od?.toDouble()
                    tv_hasil_dosis1.text = "${iod}%"
                }
                tv_hasil_kualitas1.text = "${data1.absolute_density}%"
                lyt_cardview.visibility = View.VISIBLE
                data_kosong.visibility = View.GONE
                img_save.visibility = View.VISIBLE
                getData2(date)
            } else {
                lyt_cardview.visibility = View.GONE
                data_kosong.visibility = View.VISIBLE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }

    }

    private fun getData2(date: String) {
        val db = FirebaseFirestore.getInstance()
        val lat2 = db.collection(uid).document(date+"_2")
        lat2.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data2 = JSONObject(document.data).toObject(DataLatihan::class.java)
                dataList.add(data2)
                getTotal(dataList)
                cardview2.visibility = View.VISIBLE
                tv_hasil_durasi2.text = "${data2.durasi_aktif} menit"
                if (data2.i_od == "NaN"){
                    tv_hasil_dosis2.text = "${data2.i_od}%"
                }else{
                    val iod = data2.i_od?.toDouble()
                    tv_hasil_dosis2.text = "${iod}%"
                }
                tv_hasil_kualitas2.text = "${data2.absolute_density}%"
                getData3(date)
            } else {
                cardview2.visibility = View.GONE
                cardview3.visibility = View.GONE
                cardview4.visibility = View.GONE
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    private fun getData3(date: String){
        val db = FirebaseFirestore.getInstance()
        val lat3 = db.collection(uid).document(date+"_3")
        lat3.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data3 = JSONObject(document.data).toObject(DataLatihan::class.java)
                dataList.add(data3)
                getTotal(dataList)
                cardview3.visibility = View.VISIBLE
                tv_hasil_durasi3.text = "${data3.durasi_aktif} menit"
                if (data3.i_od == "NaN"){
                    tv_hasil_dosis3.text = "${data3.i_od}%"
                }else{
                    val iod = data3.i_od?.toDouble()
                    tv_hasil_dosis3.text = "${iod}%"
                }
                tv_hasil_kualitas3.text = "${data3.absolute_density}%"
                getData4(date)
            } else {
                cardview3.visibility = View.GONE
                cardview4.visibility = View.GONE
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    private fun getData4(date: String){
        val db = FirebaseFirestore.getInstance()
        val lat4 = db.collection(uid).document(date+"_4")
        lat4.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data4 = JSONObject(document.data).toObject(DataLatihan::class.java)
                dataList.add(data4)
                getTotal(dataList)
                cardview4.visibility = View.VISIBLE
                tv_hasil_durasi4.text = "${data4.durasi_aktif} menit"
                if (data4.i_od == "NaN"){
                    tv_hasil_dosis4.text = "${data4.i_od}%"
                }else{
                    val iod = data4.i_od?.toDouble()
                    tv_hasil_dosis4.text = "${iod}%"
                }
                tv_hasil_kualitas4.text = "${data4.absolute_density}%"
                getData5(date)
            } else {
                cardview4.visibility = View.GONE
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    private fun getData5(date: String){
        val db = FirebaseFirestore.getInstance()
        val lat5 = db.collection(uid).document(date+"_5")
        lat5.get().addOnSuccessListener { document ->
            if (document.exists()) {
                data5 = JSONObject(document.data).toObject(DataLatihan::class.java)
                dataList.add(data5)
                getTotal(dataList)
                cardview5.visibility = View.VISIBLE
                tv_hasil_durasi5.text = "${data5.durasi_aktif} menit"
                if (data5.i_od == "NaN"){
                    tv_hasil_dosis5.text = "${data5.i_od}%"
                }else{
                    val iod = data5.i_od?.toDouble()
                    tv_hasil_dosis5.text = "${iod}%"
                }
                tv_hasil_kualitas5.text = "${data5.absolute_density}%"
                dialogLoading.show(false)
            } else {
                cardview5.visibility = View.GONE
                dialogLoading.show(false)
            }
        }.addOnFailureListener { exception -> Log.e("TAG", "get failed with ", exception) }
    }

    private fun getTotal(dataList: ArrayList<DataLatihan>){
        val durasiTotal =(data1.durasi_total!!.toInt() + data2.durasi_total!!.toInt() + data3.durasi_total!!.toInt() + data4.durasi_total!!.toInt() + data5.durasi_total!!.toInt())/dataList.size
        val durasiAktif = (data1.durasi_aktif!!.toInt() + data2.durasi_aktif!!.toInt() + data3.durasi_aktif!!.toInt() + data4.durasi_aktif!!.toInt() + data5.durasi_aktif!!.toInt())/dataList.size
        val durasiIstirahat = (data1.durasi_istirahat!!.toInt() + data2.durasi_istirahat!!.toInt() + data3.durasi_istirahat!!.toInt() + data4.durasi_istirahat!!.toInt() + data5.durasi_istirahat!!.toInt())/dataList.size
        val kuatlitas = (data1.absolute_density!!.toFloat() + data2.absolute_density!!.toFloat() + data3.absolute_density!!.toFloat() + data4.absolute_density!!.toFloat() + data5.absolute_density!!.toFloat())/dataList.size
        val iod = (data1.i_od!!.toFloat() + data2.i_od!!.toFloat() + data3.i_od!!.toFloat() + data4.i_od!!.toFloat() + data5.i_od!!.toFloat())/dataList.size

        hasil_total_aktif.text = "${durasiAktif} menit"
        hasil_total_durasi.text = "${durasiTotal} menit"
        hasil_total_isirahat.text = "${durasiIstirahat} menit"
        hasil_total_kualitas.text = "${kuatlitas}%"
        hasil_total_iod.text = "${iod}%"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CustomIntent.customType(this, "right-to-left")
    }
    @Suppress("DEPRECATION")
    private fun saveData(filename: String) {
        try {
            val sdcard: File = Environment.getExternalStorageDirectory()
            val dir = File(sdcard.absolutePath + "/Iod")
            if (!dir.exists()){
                dir.mkdir()
            }
            val file = File(dir, "${filename}.txt")
            var os: FileOutputStream? = null
            os = FileOutputStream(file)
            os.write("${hasil_total_durasi.text}".toByteArray())
            os.write("\n".toByteArray())
            os.write("${hasil_total_aktif.text}".toByteArray())
            os.write("\n".toByteArray())
            os.write("${hasil_total_isirahat.text}".toByteArray())
            os.write("\n".toByteArray())
            os.write("${hasil_total_kualitas.text}".toByteArray())
            os.write("\n".toByteArray())
            os.write("${hasil_total_iod.text}".toByteArray())
            os.write("\n".toByteArray())
            os.close()
            Toast.makeText(this, "Resume latihan berhasil disimpan", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}