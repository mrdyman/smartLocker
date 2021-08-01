package com.macca.smartlocker

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.macca.smartlocker.Fragment.LockerFragment
import com.macca.smartlocker.Fragment.MyLockerFragment
import com.macca.smartlocker.Fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.time_picker_dialog.*


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        bn_home.setOnNavigationItemSelectedListener(bottomNavListener)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ll_home_fragment_container, MyLockerFragment())
        fr.commit()
    }

    private val bottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFr: Fragment = MyLockerFragment()
        when (item.itemId) {
            R.id.nav_home -> {
                selectedFr = MyLockerFragment()
            }
            R.id.nav_locker -> {
                selectedFr = LockerFragment()
            }
            R.id.nav_history -> {
                selectedFr = LockerFragment()
            }
            R.id.nav_profile -> {
                selectedFr = ProfileFragment()
            }
        }

        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.ll_home_fragment_container, selectedFr)
        fr.commit()
        true
    }

    fun showTimePicker(context : Context, id: Long?){
        Log.d("TimePicker", "Time Picker is called id = $id")
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.time_picker_dialog)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        MaterialAlertDialogBuilder(context, R.style.Base_ThemeOverlay_MaterialComponents_Dialog)
            .setView(R.layout.time_picker_dialog)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .show()
    }
}