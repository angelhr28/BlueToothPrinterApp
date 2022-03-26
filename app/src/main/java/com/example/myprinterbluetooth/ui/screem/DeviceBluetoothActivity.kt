package com.example.myprinterbluetooth.ui.screem

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprinterbluetooth.databinding.ActivityDeviceBluetoothBinding

class DeviceBluetoothActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeviceBluetoothBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rcDeviceBluetooth.apply {
            this.layoutManager = layoutManager
            setHasFixedSize(true)
        }
    }

    fun getBluetoothAdapter() {
        val bluetoothManager = this.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter?.let {

        } ?: Toast.makeText(this, "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show()

        Log.e(TAG, "onCreate: LLEGO ESTO ::: ${bluetoothManager.adapter}")

    }

    override fun onBackPressed() {
        clickItem()
    }

    private fun clickItem() {
        val bundle = Bundle()
        bundle.putString(
            "DireccionDispositivo",
            "FFFF"
        )
        bundle.putString(
            "NombreDispositivo",
            "HOLA "
        )
        val intentPaAtras = Intent()
        intentPaAtras.putExtras(bundle)
        setResult(RESULT_OK, intentPaAtras)
        finish()
    }

}