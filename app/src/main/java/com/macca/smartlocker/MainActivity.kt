package com.macca.smartlocker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.macca.smartlocker.Fragment.HomeFragment
import com.macca.smartlocker.Fragment.LockerFragment
import com.macca.smartlocker.Fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bn_home.setOnNavigationItemSelectedListener(bottomNavListener)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ll_locker_container, HomeFragment())
        fr.commit()
    }

    private val bottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFr: Fragment = HomeFragment()
        when (item.itemId) {
            R.id.nav_home -> {
                selectedFr = HomeFragment()
            }
            R.id.nav_mylocker -> {
                selectedFr = LockerFragment()
            }
            R.id.nav_history -> {
                selectedFr = HomeFragment()
            }
            R.id.nav_profile -> {
                selectedFr = ProfileFragment()
            }
        }

        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.ll_locker_container, selectedFr)
        fr.commit()
        true
    }
}