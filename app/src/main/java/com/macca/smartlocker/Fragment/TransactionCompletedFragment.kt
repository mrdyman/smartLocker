package com.macca.smartlocker.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.macca.smartlocker.Adapter.TransactionCompletedAdapter
import com.macca.smartlocker.Adapter.TransactionRunningAdapter
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_transaction_completed.*
import kotlinx.android.synthetic.main.fragment_transaction_running.*

class TransactionCompletedFragment : Fragment() {

    private lateinit var transactionCompletedAdapter : TransactionCompletedAdapter
    private lateinit var databaseReference : DatabaseReference
    private lateinit var transactionCompleted : ArrayList<Transaction>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_completed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionCompleted = arrayListOf()
        transactionCompletedAdapter = TransactionCompletedAdapter(transactionCompleted)
        rv_locker_completed.setHasFixedSize(true)
        rv_locker_completed.layoutManager = LinearLayoutManager(activity)
        rv_locker_completed.adapter = transactionCompletedAdapter
        getLockerCompleted()
    }

    private fun getLockerCompleted() {
        databaseReference = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Transaction")

        val auth =  (activity as MainActivity).auth
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReference.child(userId.toString())

        dataTransaction.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(transaction: DataSnapshot) {
                transactionCompleted.clear()
                if (!transaction.exists()){
                    //data transaksi dengan user Id ini tidak ditemukan
                    Log.d("dataTransaction", "Data not found.")
                } else {
                    //data transaksi ditemukan.
                    //lakukan looping data transaksi berdasarkan user yang login
                    for (mTransaction in transaction.children){
                        //ambil data status transaksi (running/completed)
                        val lockerStatus = mTransaction.child("transaction_Status").value

                        //cek, kalo status transaksi = completed, tampilkan ke recyclerview
                        if (lockerStatus == "Completed") {
                            //status = running, masukkan data ke recyclerView
                            val transactionData = mTransaction.getValue(Transaction::class.java)
                            transactionCompleted.add(transactionData!!)
                            Log.d("transactionStatus", "Transaction is Completed")
                        }
                    }
                    rv_locker_completed?.adapter = transactionCompletedAdapter
                }
                if (transactionCompleted.isEmpty()) {
                    ll_transaction_completed_item_empty?.visibility = View.VISIBLE
                } else {
                    ll_transaction_completed_item_empty?.visibility = View.GONE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}