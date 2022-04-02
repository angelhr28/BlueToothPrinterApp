package com.example.myprinterbluetooth.utils

import android.content.Context
import android.util.Log
import java.io.UnsupportedEncodingException

class PrinterDriver(
    val context: Context,
    private val text: String,
    private val bluetoothDriver: BluetoothDriver
) {
    fun printText() {
        try {
            // Para que acepte caracteres espciales
            bluetoothDriver.outputStream.write(0x1C)
            bluetoothDriver.outputStream.write(0x2E) // Cancelamos el modo de caracteres chino (FS .)
            bluetoothDriver.outputStream.write(0x1B)
            bluetoothDriver.outputStream.write(0x74)
            bluetoothDriver.outputStream.write(0x10) // Seleccionamos los caracteres escape (ESC t n) - n = 16(0x10) para WPC1252
            bluetoothDriver.outputStream.write(getByteString(text))
            bluetoothDriver.outputStream.write("\n\n".toByteArray())
        } catch (e: Exception) {
            Log.e("TAG_DEBUG", "Error al escribir en el socket  ${e.message}")
        }
    }

    private fun getByteString(
        str: String,
        bold: Int = 0,
        font: Int = 0,
        widthsize: Int = 10,
        heigthsize: Int = 10
    ): ByteArray? {

        if ((str.isEmpty()) or (widthsize < 0) or (widthsize > 3) or (heigthsize < 0) or (heigthsize > 3) or (font < 0) or (font > 1)
        ) return null
        val strData: ByteArray? = try {
            str.toByteArray(charset("iso-8859-1"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }
        val command = ByteArray((strData?.size ?: 0) + 9)
        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30) //
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03) //
        command[0] = 27 // caracter ESC para darle comandos a la impresora
        command[1] = 69
        command[2] = bold.toByte()
        command[3] = 27
        command[4] = 77
        command[5] = font.toByte()
        command[6] = 29
        command[7] = 33
        command[8] = (intToWidth[widthsize] + intToHeight[heigthsize]).toByte()
        strData?.let { System.arraycopy(strData, 0, command, 9, it.size) }
        return command
    }


}