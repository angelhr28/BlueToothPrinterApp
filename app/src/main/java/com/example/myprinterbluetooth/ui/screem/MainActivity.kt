package com.example.myprinterbluetooth.ui.screem

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myprinterbluetooth.databinding.ActivityMainBinding
import com.example.myprinterbluetooth.utils.BluetoothDriver
import com.example.myprinterbluetooth.utils.toast
import com.example.myprinterbluetooth.utils.validatePermission

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bluetoothDriver: BluetoothDriver

    private lateinit var nameDevice: String
    private lateinit var addressDevice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bluetoothDriver = BluetoothDriver(this)
        initPermissions()
        binding.apply {
            btnDeviceSearch.setOnClickListener { onPressDeviceSearch() }
            btnPrint.setOnClickListener { onPressPrinter(edtText.text.toString().trim()) }
        }
    }

    private fun onPressDeviceSearch() =
        startForResult.launch(Intent(this, DeviceBluetoothActivity::class.java))

    private fun onPressPrinter(text: String) {
        // Imprime los escrito en el text
    }

    @SuppressLint("MissingPermission")
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                intent?.extras?.apply {
                    nameDevice = getString("name") ?: ""
                    addressDevice = getString("address") ?: ""
                }
                Thread {
                    bluetoothDriver.connection(nameDevice, addressDevice) {
                        runOnUiThread {
                            toast(it, this)
                        }
                    }
                }.start()
            }
        }


    @SuppressLint("InlinedApi")
    private fun initPermissions() {
        if (validatePermission(Manifest.permission.BLUETOOTH_CONNECT, this)) {
            bluetoothDriver.initSet()
            return
        }

        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.any { it.value == false }) {
                toast("El dispositivo necesita que acepte el permiso Bluetooth", this)
                finish()
            }
            bluetoothDriver.initSet()
        }
}