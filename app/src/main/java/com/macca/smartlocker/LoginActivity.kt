package com.macca.smartlocker

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //cek status login
        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            //user is signed in. redirect to mainActivity
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_pass.text.toString()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("Auth", "signInWithEmail:failure", it.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener {
                    Log.d("Auth", "Failure ${it.printStackTrace()}")
                }
        }

        tv_forgot_password.setOnClickListener {
            val i = Intent(this, ResetPasswordActivity::class.java)
            startActivity(i)
            finish()
        }

        tv_signup.setOnClickListener {
            val i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}