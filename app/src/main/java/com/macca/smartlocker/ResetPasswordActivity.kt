package com.macca.smartlocker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*


class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

//        val resetEmail = findViewById<View>(R.id.rl_email_reset) as RelativeLayout
//        val newPass = findViewById<View>(R.id.rl_new_pass) as RelativeLayout
//        val newPass1 = findViewById<View>(R.id.rl_new_pass1) as RelativeLayout
//
//        btn_reset_pass.text="Verify"
//
//        newPass.visibility = View.GONE
//        newPass1.visibility = View.GONE

        btn_reset_pass.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val email = et_email_reset.text
            auth.sendPasswordResetEmail("smartlockeriot@gmail.com").addOnCompleteListener { listener ->
                if (listener.isSuccessful) {
                    //sukses kirim email reset password
                    Toast.makeText(this, "Mohon periksa email anda untuk reset password", Toast.LENGTH_LONG).show()
                } else {
                    //gagal mengirim email reset password
                        Log.d("resetPassword", "failed to reset with exception : ${listener.exception}")
                    Toast.makeText(this, "Akun tidak ditemukan", Toast.LENGTH_LONG).show()
                }
            }
        }

        tv_forgot_login.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}