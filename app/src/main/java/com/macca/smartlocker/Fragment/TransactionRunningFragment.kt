package com.macca.smartlocker.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.Adapter.TransactionAdapter
import com.macca.smartlocker.Adapter.TransactionRunningAdapter
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_transaction_completed.*
import kotlinx.android.synthetic.main.fragment_transaction_running.*

class TransactionRunningFragment : Fragment() {

    private lateinit var transactionRunningAdapter : TransactionRunningAdapter
    private lateinit var databaseReference : DatabaseReference
    private lateinit var transactionRunning : ArrayList<Transaction>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_running, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionRunning = arrayListOf()
        transactionRunningAdapter = TransactionRunningAdapter(transactionRunning)
        rv_locker_running.setHasFixedSize(true)
        rv_locker_running.layoutManager = LinearLayoutManager(activity)
        rv_locker_running.adapter = transactionRunningAdapter
        getLockerRunning()
    }

    private fun getLockerRunning() {
        databaseReference = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Transaction")

        val auth =  (activity as MainActivity).auth
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReference.child(userId.toString())

        dataTransaction.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(transaction: DataSnapshot) {
                transactionRunning.clear()
                if (!transaction.exists()){
                    //data transaksi dengan user Id ini tidak ditemukan
                    Log.d("dataTransaction", "Data not found.")
                } else {
                    //data transaksi ditemukan.
                    //lakukan looping data transaksi berdasarkan user yang login
                    for (mTransaction in transaction.children){
                        //ambil data status transaksi (running/completed)
                        val lockerStatus = mTransaction.child("transaction_Status").value

                        //cek, kalo status transaksi = running, tampilkan ke recyclerview
                        if (lockerStatus == "Running") {
                            //status = running, masukkan data ke recyclerView
                            val transactionData = mTransaction.getValue(Transaction::class.java)
                            transactionRunning.add(transactionData!!)
                            Log.d("transactionStatus", "Transaction is Running")
                        }
                    }
                    rv_locker_running?.adapter = transactionRunningAdapter
                }
                if (transactionRunning.isEmpty()){
                    ll_transaction_running_item_empty?.visibility = View.VISIBLE
                } else {
                    ll_transaction_running_item_empty?.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}