package com.example.assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Hosts the CUSTOM broadcast receiver that receives text message given in SecondActivity. */
class ThirdActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_MESSAGE = "extra_message"
        const val ACTION_CUSTOM = "com.example.assignment2.CUSTOM_MESSAGE"
    }

    private var type = SecondActivity.TYPE_CUSTOM
    private var messageText: String? = null
    private var broadcastCount = 0

    private lateinit var tvTitleThird: TextView
    private lateinit var tvReceived: TextView
    private lateinit var btnTriggerBroadcast: Button

    private val customReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION_CUSTOM) {
                broadcastCount++
                val msg = intent.getStringExtra(EXTRA_MESSAGE) ?: messageText ?: ""
                val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                tvReceived.text = "Custom Broadcast Received (#$broadcastCount)\n\nMessage: \"$msg\"\nTime: $timeStr"
                Toast.makeText(context, "Broadcast #$broadcastCount received!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        findViewById<View>(R.id.root).applySystemBarPadding()

        tvTitleThird = findViewById(R.id.tvTitleThird)
        tvReceived = findViewById(R.id.tvReceived)
        btnTriggerBroadcast = findViewById(R.id.btnTriggerBroadcast)

        type = intent.getIntExtra(EXTRA_TYPE, SecondActivity.TYPE_CUSTOM)
        messageText = intent.getStringExtra(EXTRA_MESSAGE)

        if (type == SecondActivity.TYPE_CUSTOM) {
            tvTitleThird.text = "Custom Broadcast Receiver"
            tvReceived.text = "Waiting for broadcast..."
            btnTriggerBroadcast.visibility = View.VISIBLE

            btnTriggerBroadcast.setOnClickListener {
                sendCustomBroadcast(messageText ?: "")
            }
        } else {
            tvTitleThird.text = "Third Activity (Option 2 - Battery)"
            tvReceived.text = "Second option selected (Battery Notification).\nNo action performed here (Do Nothing)."
            btnTriggerBroadcast.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        if (type == SecondActivity.TYPE_CUSTOM) {
            ContextCompat.registerReceiver(
                this,
                customReceiver,
                IntentFilter(ACTION_CUSTOM),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            // Trigger initial broadcast
            if (broadcastCount == 0) {
                sendCustomBroadcast(messageText ?: "")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (type == SecondActivity.TYPE_CUSTOM) {
            try {
                unregisterReceiver(customReceiver)
            } catch (_: Exception) {
            }
        }
    }

    private fun sendCustomBroadcast(msg: String) {
        val intent = Intent(ACTION_CUSTOM).apply {
            setPackage(packageName)
            putExtra(EXTRA_MESSAGE, msg)
        }
        sendBroadcast(intent)
    }
}
