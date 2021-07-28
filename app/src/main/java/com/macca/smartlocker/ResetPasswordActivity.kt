package com.macca.smartlocker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reset_password.*


class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val resetEmail = findViewById<View>(R.id.rl_email_reset) as RelativeLayout
        val newPass = findViewById<View>(R.id.rl_new_pass) as RelativeLayout
        val newPass1 = findViewById<View>(R.id.rl_new_pass1) as RelativeLayout

        btn_reset_pass.text="Verify"

        newPass.visibility = View.GONE
        newPass1.visibility = View.GONE

        btn_reset_pass.setOnClickListener {
            val tvEmail = et_email_reset.text
            if (tvEmail.toString() == "diman"){
                resetEmail.visibility = View.GONE
                newPass.visibility = View.VISIBLE
                newPass1.visibility = View.VISIBLE
                btn_reset_pass.text = "Reset Password"
            }
        }

        tv_forgot_login.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}