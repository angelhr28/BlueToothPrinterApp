package com.example.myprinterbluetooth.ui.screem

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myprinterbluetooth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            btnDeviceSearch.setOnClickListener { onPressDeviceSearch() }
            btnPrint.setOnClickListener { onPressPrinter(edtText.text.toString().trim()) }
        }
    }


    private fun onPressDeviceSearch() {

        startForResult.launch(Intent(this, DeviceBluetoothActivity::class.java))

    }


    private fun onPressPrinter(text: String) {
        // Imprime los escrito en el text
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                Log.e(TAG, "onPressDeviceSearch: ${intent?.extras?.getString("NombreDispositivo")}")

            }

        }

}