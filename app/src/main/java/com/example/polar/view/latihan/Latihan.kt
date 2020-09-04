package com.example.polar.view.latihan

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polar.R
import com.example.polar.model.DataLatihan
import com.example.polar.support.KEY_DATA
import com.example.polar.support.dialog.DialogLoading
import com.example.polar.support.mtformat
import com.example.polar.view.Router
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_latihan.*
import polar.com.sdk.api.PolarBleApi
import polar.com.sdk.api.PolarBleApiCallback
import polar.com.sdk.api.PolarBleApiDefaultImpl
import polar.com.sdk.api.errors.PolarInvalidArgument
import polar.com.sdk.api.model.PolarDeviceInfo
import polar.com.sdk.api.model.PolarHrData
import java.util.*
import kotlin.collections.ArrayList


class Latihan : AppCompatActivity() {
    private val router by lazy { Router() }
    private val dialogLoading  by lazy { DialogLoading(this) }
    private val bAdapter = BluetoothAdapter.getDefaultAdapter();
    private val TAG = Latihan::class.java.simpleName
    lateinit var api: PolarBleApi
    var DEVICE_ID = "218DDA23" // or bt address like F5:A7:B8:EF:7A:D1 //
    var broadcastDisposable: Disposable? = null
    var ecgDisposable: Disposable? = null
    var accDisposable: Disposable? = null
    var ppgDisposable: Disposable? = null
    var ppiDisposable: Disposable? = null
    var arrayHrData = ArrayList<Long>()
    var timeBuffBreak = 0L
    var timeBuffLat = 0L

    var totalSeconds = 3600000L

    val timerAll= object: CountDownTimer(3600000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val waktu = String.format("%02d:%02d", ((totalSeconds - millisUntilFinished)/(1000*60))%60, ((totalSeconds - millisUntilFinished)/1000)%60)
            txt_total_durasi.text = waktu
            if (txt_bpm.text.toString().toInt() < txt_batas.text.toString().toInt()){
                if (timeBuffLat != 0L){
                    timeBuffBreak = (millisUntilFinished - timeBuffLat)
                }else{
                    timeBuffBreak = millisUntilFinished
                }
                val waktu = String.format("%02d:%02d", ((totalSeconds - timeBuffBreak)/(1000*60))%60, ((totalSeconds - timeBuffBreak)/1000)%60)
                txt_durasi_istirahat.text = waktu
            }else{
                if (timeBuffBreak != 0L){
                    timeBuffLat = (millisUntilFinished - timeBuffBreak)
                }else{
                    timeBuffLat = millisUntilFinished
                }
                val waktu = String.format("%02d:%02d", ((totalSeconds - timeBuffLat)/(1000*60))%60, ((totalSeconds - timeBuffLat)/1000)%60)
                txt_durasi_latihan.text = waktu
            }
        }

        override fun onFinish() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latihan)

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
        txt_batas.text = intent?.extras?.getString(KEY_DATA)!!
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
            if (btn_mulai.text.toString().equals("Mulai")){
                if (txt_device.text.toString().equals("Device")){
                    Toast.makeText(this, "Koneksikan dengan perangkat terlebih dahulu!", Toast.LENGTH_SHORT).show()
                }else{
                    timerAll.start()
                    markButtonDisable(btn_mulai)
                    markButtonEnable(btn_selesai)
                    val tempDate = Calendar.getInstance().time as Date
                    val jam = mtformat.format(tempDate)
                    txt_jam_mulai.text = jam
                }
            }else{
                val hasilData = DataLatihan().apply {
                    jam_mulai = txt_jam_mulai.text.toString()
                    jam_selesai = txt_jam_selesai.text.toString()
                    durasi_istirahat = txt_durasi_istirahat.text.toString()
                    durasi_aktif = txt_durasi_latihan.text.toString()
                    durasi_total = txt_total_durasi.text.toString()
                }
                router.toHasil(this, hasilData)
            }
        }

        btn_selesai.setOnClickListener {
            timerAll.cancel()
            markButtonEnable(btn_mulai)
            markButtonDisable(btn_selesai)
            val tempDate = Calendar.getInstance().time as Date
            val jam = mtformat.format(tempDate)
            txt_jam_selesai.text = jam
            btn_mulai.text = "Lanjut"
        }

        setSupportActionBar(toolbar_latihan)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Latihan")
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
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
                            dialogLoading.show(false)
                            img_cek.setImageDrawable(getDrawable(R.drawable.ic_check))
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_connect, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.connect_polar -> {
            dialogLoading.show(true)
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
            dialogLoading.show(true)
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

    fun markButtonEnable(button: Button) {
        button.isEnabled = true
        button.background = getDrawable(R.drawable.bg_blue_button)
    }
}
