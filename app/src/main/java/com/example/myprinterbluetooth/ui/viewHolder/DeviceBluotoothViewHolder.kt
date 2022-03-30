package com.example.myprinterbluetooth.ui.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myprinterbluetooth.databinding.ItemDeviceBluetoothBinding
import com.example.myprinterbluetooth.model.DeviceBluetooth

class DeviceBluotoothViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemDeviceBluetoothBinding.bind(view)

    fun bind(item: DeviceBluetooth, listener: (DeviceBluetooth) -> Unit) {
        binding.apply {
            txtName.text = item.name
            txtAddress.text = item.address
            root.setOnClickListener {
                listener(item)
            }
        }
    }

}
