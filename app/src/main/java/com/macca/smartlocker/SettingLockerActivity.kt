package com.macca.smartlocker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_setting_locker.*

class SettingLockerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_locker)

        displayLockerSetting()
    }

    private fun displayLockerSetting() {
        val namaLocker = intent.getStringExtra("namaLocker").toString()
        val mulai = intent.getStringExtra("mulai").toString()
        val selesai = intent.getStringExtra("selesai").toString()
        val sisaWaktu = intent.getStringExtra("sisaWaktu").toString()
        val status = intent.getStringExtra("status").toString()

        tv_detail_nama_locker.text = namaLocker
        tv_mulai.text = mulai
        tv_selesai.text = selesai
        tv_sisa_waktu_setting.text = sisaWaktu
        tv_locker_status_setting.text = status
    }
}