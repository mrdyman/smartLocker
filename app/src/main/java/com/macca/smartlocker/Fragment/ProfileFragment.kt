package com.macca.smartlocker.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.LoginActivity
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.R
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
            ll_cv_profile_container.visibility = View.GONE
        }

        iv_edit_mode_profile.setOnClickListener {
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
                            tv_user_login_name_profile.text = "Hi, "+ namaDepan
                            tv_user_login_address_profile.text = alamat.toString()
                            tv_profile_nama.text = namaUser.toString()
                            tv_profile_email.text = email.toString()
                            tv_profile_address.text = alamat.toString()
                            tv_profile_kode_pos.text = kodePos.toString()
                            tv_profile_kota.text = kota.toString()
                            tv_profile_phone_number.text = phone.toString()
                        }
                    }
                }
            }

            override fun onCancelled(dataBaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}