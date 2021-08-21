package com.macca.smartlocker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_force_open_locker.*

class ForceOpenLockerActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_force_open_locker)

        btn_alarmOff.setOnClickListener {
            resetWarningStatus()
            btn_alarmOff.text = "Alarm Turned Off"
            iv_forceOpenLocker.setImageResource(R.drawable.okay)
        }

        btn_Home.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun resetWarningStatus() {
        databaseReference = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("status")
        databaseReference.setValue("true")
    }
}