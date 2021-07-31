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
    private lateinit var databaseReferenceTransaction : DatabaseReference
    private lateinit var databaseReferenceLocker : DatabaseReference
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
        getMyLocker()
    }

    private fun getMyLocker() {
        databaseReferenceTransaction = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Transaction")

        val auth =  (activity as MainActivity).auth
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReferenceTransaction.child(userId.toString()+"s")

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
    }

    fun endLocker(id : Long?){
        //ambil data locker di firebase yang idnya di click
        databaseReferenceLocker = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Locker")
        val locker = databaseReferenceLocker.child(id.toString())

        locker.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(locker: DataSnapshot) {
                if (locker.value == null){
                    Log.d("Locker", "Locker with id $id is not found")
                } else {
                    val lockerStatus = locker.child("Status").value
                    if (lockerStatus == "Ready"){
                        Log.d("LockerStatus", "Locker $id status already ready for book.")
                    } else {
                        //update status locker ke ready(supaya bisa di book orang lain)
                        databaseReferenceLocker.child(id.toString()).child("Status").setValue("Ready")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}