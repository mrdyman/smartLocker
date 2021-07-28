package com.macca.smartlocker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.macca.smartlocker.Model.User
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btn_signup.setOnClickListener {

            //ambil data user yang di input
            val nama = et_nama.text.toString()
            val email = et_email.text.toString()
            val alamat = et_alamat.text.toString()
            val kodePos = et_kode_pos.text.toString()
            val password = et_pass.text.toString()
            val password1 = et_pass1.text.toString()

            //create akun user(insert data ke firebase authentication)
            val auth = FirebaseAuth.getInstance()
//            var userId : String? = null
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        //ambil user id yang di generate
                        val userId = auth.currentUser?.uid

                        //tampilkan pesan sukses registrasi akun baru
                        Toast.makeText(this, "Account Successful created.", Toast.LENGTH_SHORT).show()

                        //insert data user ke database
                        database = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                        val user = User(nama, email, alamat, kodePos, userId.toString())
                        database.child(nama).setValue(user).addOnSuccessListener {

                            //clear field registrasi
                            et_nama.text.clear()
                            et_email.text.clear()
                            et_alamat.text.clear()
                            et_kode_pos.text.clear()
                            et_pass.text.clear()
                            et_pass1.text.clear()

                            Toast.makeText(this, "Successfully Created Data", Toast.LENGTH_SHORT).show()
                        } .addOnFailureListener {
                            Toast.makeText(this, "Failed Created Data", Toast.LENGTH_SHORT).show()
                        }


                    } else {
                        Log.d("Failed_Ex", "${it.result}")
                        Toast.makeText(this, "Account failed to created. with meesage ${it.exception?.printStackTrace()}", Toast.LENGTH_SHORT).show()
                    }
                }

            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        tv_signup_login.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}