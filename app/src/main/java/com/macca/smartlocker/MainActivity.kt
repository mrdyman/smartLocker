package com.macca.smartlocker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.macca.smartlocker.Fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ll_locker_container, HomeFragment())
        fr.commit()

        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    // Respond to navigation home click
                    Log.d("Bnav", "Home")
                    true
                }
                R.id.mylocker -> {
                    // Respond to navigation My Locker click
                    Log.d("Bnav", "My Locker")
                    true
                }
                else -> false
            }
        }
    }
}