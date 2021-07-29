package com.macca.smartlocker.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.macca.smartlocker.LoginActivity
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {


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
    }
}