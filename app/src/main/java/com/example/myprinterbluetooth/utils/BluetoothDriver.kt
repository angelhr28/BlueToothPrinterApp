package com.example.myprinterbluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class BluetoothDriver(private val context: Context) {
    // Para la operaciones con dispositivos bluetooth
    lateinit var bluetoothAdapter: BluetoothAdapter

    private var bluetoothDevice: BluetoothDevice? = null
    private var bluetoothSocket: BluetoothSocket? = null

    // identificador unico default
    private val aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    // Para el flujo de datos de entrada y salida del socket bluetooth
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null

    fun initSet() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    @SuppressLint("MissingPermission")
    fun connection(name: String, address: String, listener: (String) -> Unit) {
        // Obtenemos el dispositivo con la direccion seleccionada en la lista
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
        try {
            Log.e("TAG", "1")
            // Conectamos los dispositivos
            bluetoothSocket =
                bluetoothDevice?.createRfcommSocketToServiceRecord(
                    aplicacionUUID
                )
            Log.e("TAG", "2  $bluetoothSocket")
            bluetoothSocket?.connect() // conectamos el socket
            Log.e("TAG", "3")
            outputStream = bluetoothSocket?.outputStream
            inputStream = bluetoothSocket?.inputStream
            Log.e("TAG", "FIN")

            //empezarEscucharDatos();
            listener("Dispositivo Conectado $name")
        } catch (e: Exception) {
            Log.e(
                "ERROR",
                "Error al conectar el dispositivo bluetooth ---  ${e.message}"
            )
            listener("No se pudo conectar el dispositivo $name")
        }
    }
}