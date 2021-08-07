package com.macca.smartlocker

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.macca.smartlocker.Fragment.MyLockerFragment
import kotlinx.android.synthetic.main.activity_setting_locker.*
import kotlinx.android.synthetic.main.end_locker_dialog.*
import kotlinx.android.synthetic.main.time_picker_dialog.*

class SettingLockerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_locker)

        displayLockerSetting()
        btnListener()
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


        val lockerStatus = tv_locker_status_setting.text
        if (lockerStatus == "LOCKED"){
            btn_open_close_locker_setting.text = "BUKA"
        } else {
            btn_open_close_locker_setting.text = "TUTUP"
        }
    }

    private fun btnListener() {
        btn_back_to_home.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        btn_end.setOnClickListener {
            showDialogEnd()
        }

        btn_open_close_locker_setting.setOnClickListener {
            openCloseLocker()
        }

        btn_tambah_waktu.setOnClickListener {
            showDialogExtend()
        }
    }

    private fun showDialogEnd() {
        val idLocker = intent.getStringExtra("idLocker").toString()
        Log.d("dialog", "Dialog End is show id = $idLocker")

        val dialog = Dialog(this)

        dialog.setContentView(R.layout.end_locker_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.btn_dialog_end_cancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.btn_dialog_end.setOnClickListener {
            val myLockerFragment = MyLockerFragment()
            myLockerFragment.endLocker(idLocker.toLong())
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

    private fun openCloseLocker() {
        val idLocker = intent.getStringExtra("idLocker").toString()

        val myLockerFragment = MyLockerFragment()
        myLockerFragment.openCloseLocker(idLocker.toLong())

        val lockerStatus = tv_locker_status_setting.text
        if (lockerStatus == "LOCKED"){
            btn_open_close_locker_setting.text = "TUTUP"
            tv_locker_status_setting.text = "UNLOCKED"
        } else {
            btn_open_close_locker_setting.text = "BUKA"
            tv_locker_status_setting.text = "LOCKED"
        }
    }

    private fun showDialogExtend() {
        val idLocker = intent.getStringExtra("idLocker").toString()
        Log.d("dialog", "Dialog End is show id = $idLocker")

        val dialog = Dialog(this)

        dialog.setContentView(R.layout.time_picker_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.btn_dialog_cancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.btn_dialog_pay.setOnClickListener {
//            val myLockerFragment = MyLockerFragment()
//            myLockerFragment.endLocker(idLocker.toLong())
//            dialog.dismiss()
//            finish()
        }
        dialog.show()
    }
}