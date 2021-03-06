package com.macca.smartlocker.Fragment

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.Adapter.LockerAdapter
import com.macca.smartlocker.Adapter.TransactionAdapter
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.Model.User
import com.macca.smartlocker.Payments.PaymentActivity
import com.macca.smartlocker.R
import com.macca.smartlocker.Util.AlarmNotification
import com.macca.smartlocker.Util.BroadcastReceiver
import com.macca.smartlocker.Util.ForcedOpenAlarm
import com.macca.smartlocker.Util.SmartLockerSharedPreferences
import kotlinx.android.synthetic.main.end_locker_dialog.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.time_picker_dialog.*
import kotlinx.android.synthetic.main.time_picker_dialog.btn_dialog_cancel
import java.lang.Thread.sleep

class MyLockerFragment : Fragment() {

    private lateinit var transactionAdapter : TransactionAdapter
    private lateinit var databaseReferenceTransaction : DatabaseReference
    private lateinit var databaseReferenceLocker : DatabaseReference
    private lateinit var databaseReferenceUser : DatabaseReference
    private lateinit var myLockerList : ArrayList<Transaction>
    private lateinit var auth: FirebaseAuth
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myLockerList = arrayListOf()
        transactionAdapter = TransactionAdapter(myLockerList)
        rv_my_locker.setHasFixedSize(true)
        rv_my_locker.layoutManager = LinearLayoutManager(activity)
        rv_my_locker.adapter = transactionAdapter
        getMyLocker()
        getUserData()
    }

    private fun getUserData(){
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        databaseReferenceUser = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Users")

        databaseReferenceUser.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(user: DataSnapshot) {
                if (user.exists()){
                    for (mUser in user.children){
                        val mUserId = mUser.child("user_id").value
                        if (userId.toString() == mUserId.toString()){
                            val namaDepan = mUser.child("nama_depan").value
                            val namaBelakang = mUser.child("nama_belakang").value
                            val namaUser = "$namaDepan" + " " + "$namaBelakang"
                            val alamat = mUser.child("alamat").value

                            //assign data dari firebase ke textview
                            tv_user_login_name?.text = namaUser.toString()
                            tv_user_login_address?.text = alamat.toString()
                        }
                    }
                }
            }

            override fun onCancelled(dataBaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getMyLocker() {
        databaseReferenceTransaction = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Transaction")

        val auth =  (activity as MainActivity).auth
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReferenceTransaction.child(userId.toString())

        dataTransaction.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(transaction: DataSnapshot) {
                myLockerList.clear()
                if (!transaction.exists()){
                    //data transaksi dengan user Id ini tidak ditemukan
                    Log.d("dataTransaction", "Data not found.")
                    //matikan pengecekan forced Open
                    val mActivity = (activity as? MainActivity)
                    mActivity?.lockerSecurityCheck(false)
                } else {
                    //jalankan pengecekan forceOpen
                    val mActivity = activity as? MainActivity
                    mActivity?.lockerSecurityCheck(true)
                    //data transaksi ditemukan.
                    //lakukan looping data transaksi berdasarkan user yang login
                        for (mTransaction in transaction.children){
                            //ambil data status transaksi (running/completed)
                            val lockerStatus = mTransaction.child("transaction_Status").value

                            //cek, kalo status transaksi = running, tampilkan ke recyclerview
                            if (lockerStatus == "Running") {
                                //status = running, masukkan data ke recyclerView
                                val transactionData = mTransaction.getValue(Transaction::class.java)
                                val endTime = mTransaction.child("selesai").value
                                myLockerList.add(transactionData!!)
                                Log.d("transactionStatus", "Transaction is Running")

                                val ma = activity as? MainActivity
                                ma?.setAlarmNotification(endTime.toString().toLong(), true)
                            } else {
                                val mas = activity as? MainActivity
                                mas?.setAlarmNotification(0, false)
                            }
                        }
                    rv_my_locker?.adapter = transactionAdapter
                }
                if (myLockerList.isEmpty()) {
                    rl_empty_data?.visibility = View.VISIBLE
                } else {
                    rl_empty_data?.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun showDialog(context: Context, id: Long?) {
        Log.d("dialog", "Dialog End is show id = $id")

        val dialog = Dialog(context)

        dialog.setContentView(R.layout.end_locker_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.btn_dialog_end_cancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.btn_dialog_end.setOnClickListener {
            endLocker(id)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun endLocker(id : Long?){
        //ambil data locker di firebase yang idnya di click
        databaseReferenceLocker = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Locker")
        val locker = databaseReferenceLocker.child(id.toString())

        //update status locker
        locker.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(locker: DataSnapshot) {
                if (locker.value == null){
                    Log.d("Locker", "Locker with id $id is not found")
                } else {
                    val lockerStatus = locker.child("Status").value
                    if (lockerStatus == "Ready "){
                        Log.d("LockerStatus", "Locker status already ready for book.")
                    } else {
                        //update status locker ke ready(supaya bisa di book orang lain)
                        databaseReferenceLocker.child(id.toString()).child("Status").setValue("Ready ")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //update status transaksi menjadi completed
        databaseReferenceTransaction = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Transaction")

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReferenceTransaction.child(userId.toString())
        dataTransaction.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    for (data in dataSnapshot.children){
                        val transactionId = data.key.toString()
                        val idLocker = data.child("id_Locker").value
                        if (idLocker == id){
                            val lockerStatus = data.child("transaction_Status").value
                            if (lockerStatus == "Running"){
                                dataTransaction.child(transactionId).child("locker_Status").setValue("LOCKED")
                                dataTransaction.child(transactionId).child("selesai").setValue(System.currentTimeMillis().toString())
                                dataTransaction.child(transactionId).child("transaction_Status").setValue("Completed")
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        //reset locker(kunci locker)
        lockerControl(id, true)
    }

    fun openCloseLocker(id : Long?){
        //update status locker di tabel transaksi
        databaseReferenceTransaction = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Transaction")

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReferenceTransaction.child(userId.toString())
        dataTransaction.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()){
                    for (data in dataSnapshot.children){
                        val transactionId = data.key.toString()
                        val idLocker = data.child("id_Locker").value
                        val transactionStatus = data.child("transaction_Status").value
                        if (idLocker == id && transactionStatus == "Running"){
                            val lockerStatus = data.child("locker_Status").value
                            if (lockerStatus == "LOCKED"){
                                dataTransaction.child(transactionId).child("locker_Status").setValue("UNLOCKED")
                            } else {
                                dataTransaction.child(transactionId).child("locker_Status").setValue("LOCKED")
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        //jalankan function tes
        lockerControl(id, false)
    }

    private fun lockerControl(id: Long?, reset: Boolean) {
        databaseReferenceTransaction = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Control")
        val locker = databaseReferenceTransaction.child(id.toString())
        locker.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(lockerSnapshot: DataSnapshot) {
                val idLocker = lockerSnapshot.key
                val lockerStatus = lockerSnapshot.value
                if (idLocker.toString() == id.toString()){
                    if (reset){
                        locker.setValue("LOCKED")
                    } else {
                        if (lockerStatus == "LOCKED"){
                            locker.setValue("UNLOCKED")
                        } else {
                            locker.setValue("LOCKED")
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}