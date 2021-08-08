package com.macca.smartlocker.Util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.macca.smartlocker.Payments.PaymentActivity
import com.macca.smartlocker.R

class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val orderId = intent?.getStringExtra("order_id")
        Log.d("broadcast_orderId", "broadcast: " + orderId.toString())
        Log.d("broadcast_orderId", "broadcast: called...!")
        val paymentActivity = PaymentActivity()
        paymentActivity.paymentStatus(context!!, orderId!!)
    }
}