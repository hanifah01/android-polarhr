package com.example.polar.view.pelatih

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.polar.R
import kotlinx.android.synthetic.main.activity_home_pelatih.*

class HomePelatih : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_pelatih)

        setSupportActionBar(toolbar_home_pelatih)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("")
        }

        getData()
    }

    private fun getData() {
        coach_name.text = "Halo, " + "pelatih"

        nama_atlit_1.text = "Nama atletnya 1"
        nama_atlit_2.text = "Nama atletnya 2"
        nama_atlit_3.text = "Nama atletnya 3"
        nama_atlit_4.text = "Nama atletnya 4"
        nama_atlit_5.text = "-"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> {
            Toast.makeText(this, "Keluar", Toast.LENGTH_LONG).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
