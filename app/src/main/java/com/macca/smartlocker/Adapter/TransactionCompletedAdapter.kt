package com.macca.smartlocker.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.list_completed_transaction.view.*

class TransactionCompletedAdapter (val transactionCompleted : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionCompletedAdapter.TransactionCompletedHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionCompletedHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_completed_transaction, parent, false)
        return TransactionCompletedHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionCompletedHolder, position: Int) {
        val transactionCompleted = transactionCompleted[position]

        holder.namaLockerCompleted.text = transactionCompleted.Nama_Locker
        holder.dateTransactionCompleted.text = transactionCompleted.Selesai

    }

    override fun getItemCount(): Int {
        return transactionCompleted.size
    }

    class TransactionCompletedHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLockerCompleted = itemView.tv_nama_locker_completed
        var dateTransactionCompleted = itemView.tv_date_transaction_completed
    }
}