package com.macca.smartlocker.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.list_my_locker.view.*
import kotlinx.android.synthetic.main.list_running_transaction.view.*

class TransactionRunningAdapter (val transactionRunning : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionRunningAdapter.TransactionRunningHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionRunningHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_running_transaction, parent, false)
        return TransactionRunningHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionRunningHolder, position: Int) {
        val transactionRunning = transactionRunning[position]

        holder.namaLockerRunning.text = transactionRunning.Nama_Locker
        holder.start.text = transactionRunning.Mulai
        holder.end.text = transactionRunning.Selesai
        holder.totalTime.text = transactionRunning.Waktu
    }

    override fun getItemCount(): Int {
        return transactionRunning.size
    }

    class TransactionRunningHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLockerRunning = itemView.tv_nama_locker_running
        var start = itemView.tv_start_time_transaction_running
        var end = itemView.tv_end_time_transaction_running
        var totalTime = itemView.tv_total_time_transaction_running
    }
}