package com.macca.smartlocker.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.LoginActivity
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.User
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var databaseReferenceUser : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val auth = (activity as MainActivity).auth
        tv_logout.setOnClickListener {
            auth.signOut()
            val i = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(i)
        }
        editButtonListener()
        getUserData()
    }

    private fun editButtonListener() {
        iv_edit_profile.setOnClickListener {
            ll_cv_profile_edit_container.visibility = View.VISIBLE
            displayEdit()
            ll_cv_profile_container.visibility = View.GONE
        }

        iv_edit_mode_profile.setOnClickListener {
            updateProfile()
            ll_cv_profile_edit_container.visibility = View.GONE
            ll_cv_profile_container.visibility = View.VISIBLE
        }
    }

    private fun getUserData() {
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        databaseReferenceUser = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")

        databaseReferenceUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(user: DataSnapshot) {
                if (user.exists()){
                    for (mUser in user.children){
                        val mUserId = mUser.child("user_id").value
                        if (userId.toString() == mUserId.toString()){
                            val namaDepan = mUser.child("nama_depan").value
                            val namaBelakang = mUser.child("nama_belakang").value
                            val namaUser = "$namaDepan"+ " " + "$namaBelakang"
                            val alamat = mUser.child("alamat").value
                            val email = mUser.child("email").value
                            val kodePos = mUser.child("kode_pos").value
                            val kota = mUser.child("kota").value
                            val phone = mUser.child("phone").value

                            //assign data dari firebase ke textview profile
                            tv_user_login_name_profile?.text = "Hi, "+ namaDepan
                            tv_user_login_address_profile?.text = alamat.toString()
                            tv_profile_nama?.text = namaUser.toString()
                            tv_profile_email?.text = email.toString()
                            tv_profile_address?.text = alamat.toString()
                            tv_profile_kode_pos?.text = kodePos.toString()
                            tv_profile_kota?.text = kota.toString()
                            tv_profile_phone_number?.text = phone.toString()
                        }
                    }
                }
            }

            override fun onCancelled(dataBaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun displayEdit(){
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        databaseReferenceUser = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")

        databaseReferenceUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(user: DataSnapshot) {
                if (user.exists()){
                    for (mUser in user.children){
                        val mUserId = mUser.child("user_id").value
                        if (userId.toString() == mUserId.toString()){
                            val namaDepan = mUser.child("nama_depan").value
                            val namaBelakang = mUser.child("nama_belakang").value
                            val alamat = mUser.child("alamat").value
                            val kodePos = mUser.child("kode_pos").value
                            val kota = mUser.child("kota").value
                            val phone = mUser.child("phone").value

                            //assign data dari firebase ke textField Edit profile
                            tv_edit_nama_depan.setText(namaDepan.toString())
                            tv_edit_nama_belakang.setText(namaBelakang.toString())
                            tv_edit_alamat.setText(alamat.toString())
                            tv_edit_kota.setText(kota.toString())
                            tv_edit_kode_pos.setText(kodePos.toString())
                            tv_edit_phone_number.setText(phone.toString())
                        }
                    }
                }
            }

            override fun onCancelled(dataBaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun updateProfile(){
        val userId = auth.currentUser?.uid

        //ambil data yang diinput user
        val alamat = tv_edit_alamat.text.toString()
        val kodePos = tv_edit_kode_pos.text.toString()
        val kota = tv_edit_kota.text.toString()
        val namaBelakang = tv_edit_nama_belakang.text.toString()
        val namaDepan = tv_edit_nama_depan.text.toString()
        val phone = tv_edit_phone_number.text.toString()
        val email = auth.currentUser?.email

        //insert data user ke database
        databaseReferenceUser = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        val user = User(alamat, email, kodePos, kota, namaBelakang, namaDepan, phone, userId.toString())
        databaseReferenceUser.child(userId.toString()).setValue(user).addOnSuccessListener {
            Log.d("profileUpdate", "profile update successful")
        } .addOnFailureListener {
            Log.d("profileUpdate", "profile update failed")
        }
    }
}