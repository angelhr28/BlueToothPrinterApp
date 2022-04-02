package com.example.myprinterbluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothDriver(private val context: Context) {
    // Para la operaciones con dispositivos bluetooth
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var bluetoothSocket: BluetoothSocket
    private lateinit var bluetoothDevice: BluetoothDevice

    // Para el flujo de datos de entrada y salida del socket bluetooth
    private lateinit var inputStream: InputStream
    lateinit var outputStream: OutputStream

    // identificador unico default
    private val aplicacionUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


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
            // Conectamos los dispositivos
            bluetoothSocket =
                bluetoothDevice.createRfcommSocketToServiceRecord(
                    aplicacionUUID
                )
            bluetoothSocket.connect() // conectamos el socket
            outputStream = bluetoothSocket.outputStream
            inputStream = bluetoothSocket.inputStream

            listener("Dispositivo Conectado $name")
        } catch (e: Exception) {
            listener("No se pudo conectar el dispositivo $name")
        }
    }
}