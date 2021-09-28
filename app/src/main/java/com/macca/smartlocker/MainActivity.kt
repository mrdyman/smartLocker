package com.macca.smartlocker

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.UniversalTimeScale
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.Fragment.HistoryFragment
import com.macca.smartlocker.Fragment.LockerFragment
import com.macca.smartlocker.Fragment.MyLockerFragment
import com.macca.smartlocker.Fragment.ProfileFragment
import com.macca.smartlocker.Model.PaymentStatus
import com.macca.smartlocker.Network.ApiConfig
import com.macca.smartlocker.Payments.PaymentActivity
import com.macca.smartlocker.Util.AlarmNotification
import com.macca.smartlocker.Util.BroadcastReceiver
import com.macca.smartlocker.Util.ForcedOpenAlarm
import com.macca.smartlocker.Util.SmartLockerSharedPreferences
import com.midtrans.sdk.corekit.utilities.Utils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var smartLockerSharedPreferences: SmartLockerSharedPreferences
    private lateinit var databaseReferenceTransaction : DatabaseReference

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

    fun setAlarmNotification(endTime : Long, enable: Boolean){
        Log.d("alarmLog", "set the alarm")
        Log.d("alarmLog", endTime.toString())
        createAlarmChannel()
        val i = Intent(this, AlarmNotification::class.java)
        val pi = PendingIntent.getBroadcast(this, 0, i, 0)
        val am : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (enable){
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTime - 600000, pi)
        } else {
            am.cancel(pi)
        }
    }

    private fun createAlarmChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "Smart-Locker-Alarm"
            val description = "Channel for Alarm SmartLocker"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Smart-Locker", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun lockerSecurityCheck(enable : Boolean){
        if (enable){
            enableAlarm(this)
            securityCheck(this)
        } else {
            disableAlarm(this)
        }
    }

    fun securityCheck(context: Context){
            Log.d("warningLog", "security check called..")
            Log.d("securityCheck", "checking locker security...")
            databaseReferenceTransaction = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("status")
            val status = databaseReferenceTransaction
            status.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataStatus: DataSnapshot) {
                    val forcedOpen = dataStatus.value
                    sendAlarm(context, forcedOpen.toString())
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    @SuppressLint("ShortAlarm")
    private fun sendAlarm(context: Context, alert: String){
        createNotificationChannel(context)
        Log.d("warningLog", "sending Alarm..")
        val i = Intent (context, ForcedOpenAlarm::class.java)
        i.putExtra("alert", alert)
        val pi = PendingIntent.getBroadcast(context, 111, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val am : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi)
    }

    private fun createNotificationChannel(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "Smart-Locker-Alarm"
            val description = "Channel for Alarm SmartLocker"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Warning-Locker", name, importance)
            channel.description = description
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun enableAlarm(context: Context) {
        val receiver = ComponentName(context, ForcedOpenAlarm::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        Log.d("warningLog", "alarm enabled")
    }

    fun disableAlarm(context: Context) {
        val receiver = ComponentName(context, ForcedOpenAlarm::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        Log.d("warningLog", "alarm disabled")
    }
}