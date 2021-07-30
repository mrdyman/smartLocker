package com.macca.smartlocker.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.macca.smartlocker.Adapter.TransactionAdapter
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_home.*

class MyLockerFragment : Fragment() {

    private lateinit var transactionAdapter : TransactionAdapter
    private lateinit var databaseReference : DatabaseReference
    private lateinit var myLockerList : ArrayList<Transaction>

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
        getLocker()
    }

    private fun getLocker() {
        databaseReference = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Transaction")

        val auth =  (activity as MainActivity).auth
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReference.child(userId.toString()+"s")

        dataTransaction.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(transaction: DataSnapshot) {
                if (transaction.value == null){
                    //data transaksi dengan user Id ini tidak ditemukan
                    Log.d("dataTransaction", "Data not found.")
                } else {
                    //data transaksi ditemukan.
                    //lakukan looping data transaksi berdasarkan user yang login
                        for (mTransaction in transaction.children){
                            //ambil data status transaksi (running/completed)
                            val lockerStatus = mTransaction.child("Transaction_Status").value

                            //cek, kalo status transaksi = running, tampilkan ke recyclerview
                            if (lockerStatus == "Running") {
                                //status = running, masukkan data ke recyclerView
                                val transactionData = mTransaction.getValue(Transaction::class.java)
                                myLockerList.add(transactionData!!)
                                Log.d("transactionStatus", "Transaction is Running")
                            }
                        }
                    rv_my_locker.adapter = transactionAdapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                if (dataSnapshot.exists()){
//
//                    for (data in dataSnapshot.children){
//                        val locker = data.getValue(Transaction::class.java)
//                        myLockerList.add(locker!!)
//                    }
//                    rv_my_locker.adapter = transactionAdapter
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.d("Firebase", "Database Error with message ${databaseError.message}")
//            }
//
//        })
    }
}