package com.example.polar.view.latihan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.polar.R
import com.example.polar.support.dialog.DialogLoading
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_petunjuk_latihan.*
import kotlinx.android.synthetic.main.activity_petunjuk_pemanasan.*
//import kotlinx.android.synthetic.main.activity_petunjuk_pemanasan.connect_button
import kotlinx.android.synthetic.main.activity_petunjuk_pemanasan.txt_bpm
import kotlinx.android.synthetic.main.activity_petunjuk_pemanasan.txt_device
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

    private val dialogLoading  by lazy { DialogLoading(this) }

    private val TAG = PetunjukPemanasan::class.java.simpleName
    lateinit var api: PolarBleApi
    var DEVICE_ID = "218DDA23" // or bt address like F5:A7:B8:EF:7A:D1 //
    var broadcastDisposable: Disposable? = null
    var ecgDisposable: Disposable? = null
    var accDisposable: Disposable? = null
    var ppgDisposable: Disposable? = null
    var ppiDisposable: Disposable? = null
    var arrayHrData = ArrayList<Long>()


    var startTime: Long = 0
    var timerHandler: Handler = Handler()
    var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            val millis = System.currentTimeMillis() - startTime
            val seconds = (millis / 1000)  % 60
            if (seconds in 1..10){
                txt_detik.setText(String.format("00:%02d", seconds))
                arrayHrData.add(seconds)
                markButtonDisable(btn_mulai)
            }
            if (seconds > 10){
                btn_mulai.setText(arrayHrData.average().toString())
            }
            timerHandler.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petunjuk_pemanasan)
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

        /*connect_button.setOnClickListener{
            connect()
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && savedInstanceState == null) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1
            )
        }

        btn_mulai.setOnClickListener {
            startTime = System.currentTimeMillis()
            timerHandler.postDelayed(timerRunnable, 0)
        }

        setSupportActionBar(toolbar_petunjukpemanasan)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Latihan")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        txt_lanjut.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, Latihan::class.java))
            CustomIntent.customType(this, "left-to-right") })
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
                            Log.d(TAG, "HR BROADCAST " + polarBroadcastData.polarDeviceInfo.deviceId + " HR: " + polarBroadcastData.hr + " batt: " + polarBroadcastData.batteryStatus)
                        }, { throwable -> Log.e(TAG, "" + throwable.localizedMessage) }
                    ) { Log.d(TAG, "complete") }
                } else {
//                        Toast.makeText(this, "Hidupkan Bluetooth Terlebih Dahulu", Toast.LENGTH_LONG).show()
                    broadcastDisposable!!.dispose()
                    null
                }
        } catch (polarInvalidArgument: PolarInvalidArgument) {
            polarInvalidArgument.printStackTrace()
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
            connect()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun markButtonDisable(button: Button) {
        button.isEnabled = false
        //button.setTextColor(R.color.white)
        button.setBackgroundColor(R.color.grey)
    }

}
