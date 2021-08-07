package com.macca.smartlocker.Fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.Adapter.LockerAdapter
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.Locker
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.Model.User
import com.macca.smartlocker.Payments.PaymentActivity
import com.macca.smartlocker.R
import com.macca.smartlocker.Util.SmartLockerSharedPreferences
import kotlinx.android.synthetic.main.fragment_locker.*
import kotlinx.android.synthetic.main.time_picker_dialog.*
import java.sql.Timestamp

class LockerFragment : Fragment() {

    private lateinit var lockerAdapter : LockerAdapter
    private lateinit var databaseReference : DatabaseReference
    private lateinit var databaseReferenceTransaction : DatabaseReference
    private lateinit var lockerList : ArrayList<Locker>
    private lateinit var auth: FirebaseAuth
    private lateinit var smartLockerSharedPreferences: SmartLockerSharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lockerList = arrayListOf()
        lockerAdapter = LockerAdapter(lockerList)
        rv_list_locker_available.setHasFixedSize(true)
        rv_list_locker_available.layoutManager = LinearLayoutManager(activity)
        rv_list_locker_available.adapter = lockerAdapter
        getLocker()
    }

    fun getLocker(){
        databaseReference = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Locker")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                lockerList.clear()
                if (dataSnapshot.exists()){
                    for (data in dataSnapshot.children){
                        //ambil data status locker (ready/booked)
                        val lockerStatus = data.child("Status").value

                        //cek, kalo status locker = ready, tampilkan ke recyclerview
                        if (lockerStatus == "Ready") {
                            //status = Ready, masukkan data ke recyclerView
                            val lockerData = data.getValue(Locker::class.java)
                            lockerList.add(lockerData!!)
                            Log.d("lockerStatus", "Locker is Ready")
                        }
                    }
                    rv_list_locker_available?.adapter = lockerAdapter

                    if (lockerList.isEmpty()){
                        rl_empty_data_locker?.visibility = View.VISIBLE
                    } else {
                        rl_empty_data_locker?.visibility = View.GONE
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase", "Database Error with message ${databaseError.message}")
            }

        })
    }

    fun showDialog(context: Context, id: Long?, namaLocker : String?) {
        Log.d("TimePicker", "Time Picker is called id = $id")

        val dialog = Dialog(context)

        dialog.setContentView(R.layout.time_picker_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val lockerTime = arrayOf("30 Menit", "60 Menit", "120 Menit")
        val adapter = ArrayAdapter(context, R.layout.dropdown_locker_time, lockerTime)
        val tvTime = dialog.tv_locker_time
        tvTime.setAdapter(adapter)

        dialog.btn_dialog_cancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.btn_dialog_pay.setOnClickListener {
            val timeLocker = tvTime.text.toString()
            //create transaction unique id
            val transactionId = "MaccaLab-"+ System.currentTimeMillis().toString()

            val i = Intent(context, PaymentActivity::class.java)

            //masukkan data ke sharepreference
            smartLockerSharedPreferences = SmartLockerSharedPreferences(context)
            smartLockerSharedPreferences.transactionId = transactionId
            smartLockerSharedPreferences.itemId = id.toString()
            smartLockerSharedPreferences.itemName = namaLocker
            smartLockerSharedPreferences.duration = timeLocker

            context.startActivity(i)
            dialog.dismiss()
        }

        dialog.show()
    }
}