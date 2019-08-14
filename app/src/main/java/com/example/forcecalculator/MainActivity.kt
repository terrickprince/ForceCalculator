package com.example.forcecalculator

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    var m_bluetoothAdapter:BluetoothAdapter?=null
    lateinit var pairedBluetoothDevices:Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH=1

    companion object{
        val EXTRA_ADDRESS:String="Device_Address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter==null){
            toast("Bluetooth adapter is not found")
            return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
