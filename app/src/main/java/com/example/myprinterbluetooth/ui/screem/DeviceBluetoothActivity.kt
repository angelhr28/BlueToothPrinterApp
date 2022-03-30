package com.example.myprinterbluetooth.ui.screem

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprinterbluetooth.databinding.ActivityDeviceBluetoothBinding
import com.example.myprinterbluetooth.model.DeviceBluetooth
import com.example.myprinterbluetooth.ui.adapter.DeviceBluetoothAdapter
import com.example.myprinterbluetooth.utils.BluetoothDriver
import com.example.myprinterbluetooth.utils.toast
import com.example.myprinterbluetooth.utils.validatePermission

class DeviceBluetoothActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceBluetoothBinding

    private var list: MutableList<DeviceBluetooth> = mutableListOf()
    private lateinit var bluetoothDriver: BluetoothDriver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bluetoothDriver = BluetoothDriver(this)

        initPermissions()
    }

    @SuppressLint("InlinedApi")
    private fun initPermissions() {
        if (validatePermission(Manifest.permission.BLUETOOTH_CONNECT, this)) {
            getBluetoothAdapter()
            return
        }

        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
            )
        )
    }

    private fun getBluetoothAdapter() {
        bluetoothDriver.initSet()
        if (!bluetoothDriver.bluetoothAdapter.isEnabled) {
            requestPermissions()
            return
        }

        setData(bluetoothDriver.bluetoothAdapter)
    }

    private fun requestPermissions() {
        val activeBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        requestBluetooth.launch(activeBluetoothIntent)
    }

    @SuppressLint("MissingPermission")
    private fun setData(adapter: BluetoothAdapter) {

        if (!adapter.isEnabled) return

        val devicesSync = adapter.bondedDevices

        if (devicesSync.size == 0) return

        for (device in devicesSync) {
            list.add(DeviceBluetooth(device.name, device.address))
        }

        binding.rcDeviceBluetooth.apply {
            layoutManager = LinearLayoutManager(this@DeviceBluetoothActivity)
            setHasFixedSize(true)
            binding.rcDeviceBluetooth.adapter = DeviceBluetoothAdapter(list) {
                val bundle = Bundle()
                bundle.apply {
                    putString("name", it.name)
                    putString("address", it.address)
                }

                val intent = Intent()
                intent.putExtras(bundle)
                setResult(RESULT_OK, intent)
                toast("Retorno esto :: ${it.name} -- ${it.address}", this@DeviceBluetoothActivity)
                finish()
            }
        }
    }

    private val requestBluetooth = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setData(bluetoothDriver.bluetoothAdapter)
            return@registerForActivityResult
        }

        toast("El dispositivo necesita que active el Bluetooth", this)
        requestPermissions()
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.entries.any { it.value == false }) {
                toast("El dispositivo necesita que acepte el permiso Bluetooth", this)
                finish()
            }

            getBluetoothAdapter()
        }
}