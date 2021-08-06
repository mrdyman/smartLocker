package com.macca.smartlocker.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.macca.smartlocker.Adapter.TransactionPendingAdapter
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_transaction_pending.*

class TransactionPendingFragment : Fragment() {

    private lateinit var transactionPendingAdapter : TransactionPendingAdapter
    private lateinit var databaseReference : DatabaseReference
    private lateinit var transactionPending : ArrayList<Transaction>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionPending = arrayListOf()
        transactionPendingAdapter = TransactionPendingAdapter(transactionPending)
        rv_locker_pending.setHasFixedSize(true)
        rv_locker_pending.layoutManager = LinearLayoutManager(activity)
        rv_locker_pending.adapter = transactionPendingAdapter
        getLockerPending()
    }

    private fun getLockerPending() {
        databaseReference = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Transaction")

        val auth =  (activity as MainActivity).auth
        val userId = auth.currentUser?.uid

        val dataTransaction = databaseReference.child(userId.toString())

        dataTransaction.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(transaction: DataSnapshot) {
                transactionPending.clear()
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
                        if (lockerStatus == "Pending") {
                            //status = running, masukkan data ke recyclerView
                            val transactionData = mTransaction.getValue(Transaction::class.java)
                            transactionPending.add(transactionData!!)
                            Log.d("transactionStatus", "Transaction is Pending")
                        }
                    }
                    rv_locker_pending?.adapter = transactionPendingAdapter

                    if (transactionPending.isEmpty()){
                        ll_transaction_pending_item_empty?.visibility = View.VISIBLE
                    } else {
                        ll_transaction_pending_item_empty?.visibility = View.GONE
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}