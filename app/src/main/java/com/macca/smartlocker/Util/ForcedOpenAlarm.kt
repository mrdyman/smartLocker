package com.macca.smartlocker.Util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.macca.smartlocker.ForceOpenLockerActivity
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.R

class ForcedOpenAlarm : BroadcastReceiver () {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("warningLog", "Forced Open called..")
        val alert = intent!!.getStringExtra("alert")
        Log.d("warningLog", alert.toString())
        if (alert == "false"){
//            Toast.makeText(context, "Warning... Locker is force Open!!!", Toast.LENGTH_LONG).show()
            showNotification(context, intent)
        } else if (alert == "true") {
//            Toast.makeText(context, "<<<<<AMAN>>>>", Toast.LENGTH_LONG).show()
        }
        val mainActivity = MainActivity()
        mainActivity.securityCheck(context!!)
    }

    private fun showNotification(context: Context?, intent: Intent?){
        val i = Intent(context, ForceOpenLockerActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pi = PendingIntent.getActivity(context, 0, i, 0)

        val builder = NotificationCompat.Builder(context!!, "Warning-Locker")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("WARNING!!!. percobaan pembobolan terdeteksi")
            .setContentText("Segera periksa locker dan selamatkan benda berharga")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)

        val notifManager = NotificationManagerCompat.from(context)
        notifManager.notify(123, builder.build())
    }
}