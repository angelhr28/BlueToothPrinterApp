package com.example.myprinterbluetooth.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprinterbluetooth.R
import com.example.myprinterbluetooth.model.DeviceBluetooth
import com.example.myprinterbluetooth.ui.viewHolder.DeviceBluotoothViewHolder

class DeviceBluetoothAdapter(
    private val list: List<DeviceBluetooth>,
    private val listener: (DeviceBluetooth) -> Unit
) :
    RecyclerView.Adapter<DeviceBluotoothViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceBluotoothViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DeviceBluotoothViewHolder(
            layoutInflater.inflate(
                R.layout.item_device_bluetooth,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DeviceBluotoothViewHolder, position: Int) =
        holder.bind(list[position], listener)

    override fun getItemCount(): Int = list.size
}