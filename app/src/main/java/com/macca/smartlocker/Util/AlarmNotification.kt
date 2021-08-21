package com.macca.smartlocker.Util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Payments.PaymentActivity
import com.macca.smartlocker.R

class AlarmNotification : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pi = PendingIntent.getActivity(context, 0, i, 0)

        val builder = NotificationCompat.Builder(context!!, "Smart-Locker")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Waktu Kamu tersisa 10 menit lagi!")
            .setContentText("Segera ambil barang didalam locker")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)

        val notifManager = NotificationManagerCompat.from(context)
        notifManager.notify(123, builder.build())
    }
}