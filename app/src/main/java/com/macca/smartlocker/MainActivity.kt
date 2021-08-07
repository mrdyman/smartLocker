package com.macca.smartlocker

import android.Manifest
import android.content.pm.PackageManager
import android.icu.util.UniversalTimeScale
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.macca.smartlocker.Fragment.HistoryFragment
import com.macca.smartlocker.Fragment.LockerFragment
import com.macca.smartlocker.Fragment.MyLockerFragment
import com.macca.smartlocker.Fragment.ProfileFragment
import com.macca.smartlocker.Util.SmartLockerSharedPreferences
import com.midtrans.sdk.corekit.utilities.Utils
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var smartLockerSharedPreferences: SmartLockerSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
        }

        auth = FirebaseAuth.getInstance()

        smartLockerSharedPreferences = SmartLockerSharedPreferences(this)
        //reset data pada sharePreference
        smartLockerSharedPreferences.transactionId = null
        smartLockerSharedPreferences.itemId = null
        smartLockerSharedPreferences.itemName = null
        smartLockerSharedPreferences.duration = null

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
                selectedFr = HistoryFragment()
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
}