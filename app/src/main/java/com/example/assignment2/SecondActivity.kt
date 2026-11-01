package com.example.assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

/**
 * Option 1 (Custom): Take text from user -> proceed to ThirdActivity.
 * Option 2 (Battery): Receive system battery percentage broadcast -> proceed to ThirdActivity.
 */
class SecondActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_CUSTOM = 0
        const val TYPE_BATTERY = 1
    }

    private var type = TYPE_CUSTOM
    private var batteryRegistered = false
    private lateinit var tvBattery: TextView

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            if (level >= 0 && scale > 0) {
                val percentage = level * 100 / scale
                tvBattery.text = "Battery Status: $percentage%"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        findViewById<View>(R.id.root).applySystemBarPadding()

        type = intent.getIntExtra(EXTRA_TYPE, TYPE_CUSTOM)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val customGroup = findViewById<View>(R.id.customGroup)
        val batteryGroup = findViewById<View>(R.id.batteryGroup)
        tvBattery = findViewById(R.id.tvBattery)

        if (type == TYPE_CUSTOM) {
            tvTitle.text = "1. Custom Broadcast Receiver"
            customGroup.visibility = View.VISIBLE
            batteryGroup.visibility = View.GONE

            val etMessage = findViewById<EditText>(R.id.etMessage)
            findViewById<Button>(R.id.btnSend).setOnClickListener {
                val text = etMessage.text.toString()
                if (text.isBlank()) {
                    etMessage.error = "Enter some text"
                    return@setOnClickListener
                }

                val intent = Intent(this, ThirdActivity::class.java).apply {
                    putExtra(ThirdActivity.EXTRA_TYPE, TYPE_CUSTOM)
                    putExtra(ThirdActivity.EXTRA_MESSAGE, text)
                }
                startActivity(intent)
            }
        } else {
            tvTitle.text = "2. System Battery Notification"
            customGroup.visibility = View.GONE
            batteryGroup.visibility = View.VISIBLE

            findViewById<Button>(R.id.btnProceedBattery).setOnClickListener {
                val intent = Intent(this, ThirdActivity::class.java).apply {
                    putExtra(ThirdActivity.EXTRA_TYPE, TYPE_BATTERY)
                }
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (type == TYPE_BATTERY) {
            ContextCompat.registerReceiver(
                this,
                batteryReceiver,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED),
                ContextCompat.RECEIVER_EXPORTED
            )
            batteryRegistered = true
        }
    }

    override fun onStop() {
        super.onStop()
        if (batteryRegistered) {
            unregisterReceiver(batteryReceiver)
            batteryRegistered = false
        }
    }
}
