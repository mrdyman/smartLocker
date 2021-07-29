package com.macca.smartlocker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.macca.smartlocker.Fragment.HomeFragment
import com.macca.smartlocker.Fragment.DetailLockerFragment
import com.macca.smartlocker.Fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        bn_home.setOnNavigationItemSelectedListener(bottomNavListener)

        val fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.ll_home_fragment_container, HomeFragment())
        fr.commit()
    }

    private val bottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFr: Fragment = HomeFragment()
        when (item.itemId) {
            R.id.nav_home -> {
                selectedFr = HomeFragment()
            }
            R.id.nav_mylocker -> {
                selectedFr = DetailLockerFragment()
            }
            R.id.nav_history -> {
                selectedFr = HomeFragment()
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