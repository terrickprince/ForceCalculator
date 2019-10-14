package com.example.forcecalculator

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    private val DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val ADDRESS_DEVICE = "74:47:09:0D:2D:C6"
    private val series: LineGraphSeries<DataPoint> = LineGraphSeries <DataPoint>()
    private lateinit var inputStream: InputStream
    private var isRead:Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = findViewById<Button>(R.id.button) as Button
        val txt = findViewById<TextView>(R.id.textView) as TextView
        txt.setTextColor(Color.BLACK)
        start.setOnClickListener {
            isRead = true
            startReadingData()
        }
        val stop = findViewById<Button>(R.id.button3) as Button
        stop.setOnClickListener{
            this.textView.setText("Stopped")
            isRead = false;
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val findMenuItems = menuInflater
        findMenuItems.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun onClickBluetoothConnect(){
        val txt = findViewById<TextView>(R.id.textView) as TextView
        if(BluetoothAdapter.getDefaultAdapter()!=null && BluetoothAdapter.getDefaultAdapter().isEnabled){
            val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(ADDRESS_DEVICE)
            if(device!=null && BluetoothAdapter.getDefaultAdapter().bondedDevices.contains(device)){
                connect(BluetoothAdapter.getDefaultAdapter(),device)
            }
            else{
                displayDialogueBox("Device not paired")
            }
        }
        else{
            displayDialogueBox("Bluetooth Adapter not enabled")
        }
    }

    fun startReadingData(){
        callback(this.textView)
    }

    fun callback (view:TextView){
        val mRunnable = object : Runnable {
            override fun run() {
                if(isRead) {
                    view.setText(recurSive())
                    view.postDelayed(this, 200)
                }
                else{
                    view.removeCallbacks(this)
                }
            }
        };
        view.post(mRunnable)
    }


    fun recurSive():String{
        try{
            if(inputStream!=null){
                val rawBytes = ByteArray(256)
                inputStream.read(rawBytes)
                return String(rawBytes)
            }
        }
        catch (ex:UninitializedPropertyAccessException){

        }
        return "Not connected with bluetooth"
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.connect -> {
            onClickBluetoothConnect()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    @Throws(IOException::class)
    private fun connect(adapter:BluetoothAdapter,bluetoothDevice: BluetoothDevice) {
        try {
            val bluetoothSocket: BluetoothSocket =
                bluetoothDevice.createInsecureRfcommSocketToServiceRecord(DEFAULT_UUID)
            adapter.cancelDiscovery()
            bluetoothSocket.connect()
            inputStream = bluetoothSocket.inputStream
        }
        catch(ex:Exception){
            displayDialogueBox("Unexpected Exception " + ex.localizedMessage)
        }
    }


    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun displayDialogueBox(message:String){
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(message)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Warning")
        // show alert dialog
        alert.show()
    }

}
