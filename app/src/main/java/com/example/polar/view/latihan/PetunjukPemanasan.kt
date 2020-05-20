package com.example.polar.view.latihan

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.polar.R
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.view.Router
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_petunjuk_pemanasan.*
import maes.tech.intentanim.CustomIntent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import polar.com.sdk.api.PolarBleApi
import polar.com.sdk.api.PolarBleApiCallback
import polar.com.sdk.api.PolarBleApiDefaultImpl
import polar.com.sdk.api.errors.PolarInvalidArgument
import polar.com.sdk.api.model.PolarDeviceInfo
import polar.com.sdk.api.model.PolarHrData
import java.util.*

class PetunjukPemanasan : AppCompatActivity() {
    private val router by lazy { Router() }
    private val dialogLoading  by lazy { DialogLoading(this) }
    val bAdapter = BluetoothAdapter.getDefaultAdapter();

    private val TAG = PetunjukPemanasan::class.java.simpleName
    lateinit var api: PolarBleApi
    var DEVICE_ID = "218DDA23" // or bt address like F5:A7:B8:EF:7A:D1 //
    var broadcastDisposable: Disposable? = null
    var ecgDisposable: Disposable? = null
    var accDisposable: Disposable? = null
    var ppgDisposable: Disposable? = null
    var ppiDisposable: Disposable? = null
    var arrayHrData = ArrayList<Long>()

    val timer = object: CountDownTimer(5000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            txt_detik.setText(String.format("00:%02d", millisUntilFinished/1000 ))
            arrayHrData.add(txt_bpm.text.toString().toLong())
            markButtonDisable(btn_mulai)
            pg_detik.progress = (millisUntilFinished/1000).toInt()
            txt_infobpm.text = "Detak jantung"
            txt_lanjut.isEnabled = false
        }

        override fun onFinish() {
            lyt_rata.visibility = View.VISIBLE
            lyt_prog.visibility = View.GONE
            txt_infobpm.text = "Rata-rata detak jantung"
            txt_bpm_rt.text = arrayHrData.average().toInt().toString()
            txt_lanjut.isEnabled = true
            btn_mulai.background = ContextCompat.getDrawable(applicationContext, R.drawable.bg_blue_button)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petunjuk_pemanasan)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && savedInstanceState == null) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }
        setupView()
    }

    private fun setupView() {
        api = PolarBleApiDefaultImpl.defaultImplementation(this, PolarBleApi.ALL_FEATURES)
        api.setPolarFilter(false)
        api.setApiLogger { s -> Log.d(TAG, s) }
        Log.d(TAG, "version: " + PolarBleApiDefaultImpl.versionInfo())
        api.setApiCallback(object : PolarBleApiCallback() {
            override fun blePowerStateChanged(powered: Boolean) {
                Log.d(TAG, "BLE power: $powered")
            }

            override fun deviceConnected(polarDeviceInfo: PolarDeviceInfo) {
                Log.d(TAG, "CONNECTED: " + polarDeviceInfo.deviceId)
                DEVICE_ID = polarDeviceInfo.deviceId
            }

            override fun deviceConnecting(polarDeviceInfo: PolarDeviceInfo) {
                Log.d(TAG, "CONNECTING: " + polarDeviceInfo.deviceId)
                DEVICE_ID = polarDeviceInfo.deviceId
            }

            override fun deviceDisconnected(polarDeviceInfo: PolarDeviceInfo) {
                Log.d(TAG, "DISCONNECTED: " + polarDeviceInfo.deviceId)
                ecgDisposable = null
                accDisposable = null
                ppgDisposable = null
                ppiDisposable = null
            }

            override fun ecgFeatureReady(identifier: String) {
                Log.d(TAG, "ECG READY: $identifier")
                // ecg streaming can be started now if needed
            }

            override fun accelerometerFeatureReady(identifier: String) {
                Log.d(TAG, "ACC READY: $identifier")
                // acc streaming can be started now if needed
            }

            override fun ppgFeatureReady(identifier: String) {
                Log.d(TAG, "PPG READY: $identifier")
                // ohr ppg can be started
            }

            override fun ppiFeatureReady(identifier: String) {
                Log.d(TAG, "PPI READY: $identifier")
                // ohr ppi can be started
            }

            override fun biozFeatureReady(identifier: String) {
                Log.d(TAG, "BIOZ READY: $identifier")
                // ohr ppi can be started
            }

            override fun hrFeatureReady(identifier: String) {
                Log.d(TAG, "HR READY: $identifier")
                // hr notifications are about to start
            }

            override fun disInformationReceived(identifier: String, uuid: UUID, value: String) {
                Log.d(TAG, "uuid: $uuid value: $value")
            }

            override fun batteryLevelReceived(identifier: String, level: Int) {
                Log.d(TAG, "BATTERY LEVEL: $level")
            }

            override fun hrNotificationReceived(identifier: String, data: PolarHrData) {
                Log.d(TAG, "HR value: " + data.hr + " rrsMs: " + data.rrsMs + " rr: " + data.rrs + " contact: " + data.contactStatus + "," + data.contactStatusSupported)
            }

            override fun polarFtpFeatureReady(s: String) {
                Log.d(TAG, "FTP ready")
            }
        })



        btn_mulai.setOnClickListener {
            if (txt_device.text.toString().equals("Device")){
                Toast.makeText(this, "Koneksikan dengan perangkat terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }else{
                timer.start()
            }
        }

        setSupportActionBar(toolbar_petunjukpemanasan)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Latihan")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        txt_lanjut.setOnClickListener{
            router.toLatihan(this, txt_bpm_rt.text.toString())
        }
    }

    private fun connect(){
        try {
            api.connectToDevice(DEVICE_ID)
            broadcastDisposable =
                if (broadcastDisposable == null) {
                    api.startListenForPolarHrBroadcasts(null).subscribe(
                        { polarBroadcastData ->
                            txt_device.text = polarBroadcastData.polarDeviceInfo.deviceId
                            txt_bpm.text = polarBroadcastData.hr.toString()
                            dialogLoading.showDialog(false)
                            img_cek.setImageDrawable(getDrawable(R.drawable.ic_check))
                            Log.d(TAG, "HR BROADCAST " + polarBroadcastData.polarDeviceInfo.deviceId + " HR: " + polarBroadcastData.hr + " batt: " + polarBroadcastData.batteryStatus)
                        }, { throwable -> Log.e(TAG, "" + throwable.localizedMessage) }
                    ) { Log.d(TAG, "complete") }
                } else {
//                        Toast.makeText(this, "Hidupkan Bluetooth Terlebih Dahulu", Toast.LENGTH_LONG).show()
                    dialogLoading.showDialog(false)
                    broadcastDisposable!!.dispose()
                    null
                }
        } catch (polarInvalidArgument: PolarInvalidArgument) {
            polarInvalidArgument.printStackTrace()
            dialogLoading.showDialog(false)
            Toast.makeText(this, "Gagal Menyambungkan Device", Toast.LENGTH_LONG).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            Log.d(TAG, "bt ready")
        }
    }

    override fun onPause() {
        super.onPause()
        api.backgroundEntered()
    }

    override fun onResume() {
        super.onResume()
        api.foregroundEntered()
    }

    override fun onDestroy() {
        super.onDestroy()
        api.shutDown()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_connect, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.connect_polar -> {
            dialogLoading.showDialog(true)
            if(bAdapter == null)
            {
                Toast.makeText(getApplicationContext(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show()
            }
            else{
                if(!bAdapter.isEnabled()){
                    startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE),1)
                    cekGps()
                }else{
                    cekGps()
                }
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun cekGps(){
        if (isLocationEnabled()){
            connect()
        }else{
            Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            dialogLoading.showDialog(true)
            connect()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun markButtonDisable(button: Button) {
        button.isEnabled = false
        button.background = getDrawable(R.color.grey)
    }

}
