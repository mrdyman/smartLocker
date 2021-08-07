package com.macca.smartlocker.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.list_my_locker.view.*
import kotlinx.android.synthetic.main.list_running_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionRunningAdapter (val transactionRunning : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionRunningAdapter.TransactionRunningHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionRunningHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_running_transaction, parent, false)
        return TransactionRunningHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionRunningHolder, position: Int) {
        val transactionRunning = transactionRunning[position]

        val startTime = Date(transactionRunning.Mulai!!.toLong())
        val endTime = Date(transactionRunning.Selesai!!.toLong())
        val timeMulai = convertDate(startTime)
        val timeSelesai = convertDate(endTime)

        holder.namaLockerRunning.text = transactionRunning.Nama_Locker
        holder.start.text = timeMulai
        holder.end.text = timeSelesai
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

    private fun convertDate(time : Date) : String{
        val timeZoneDate = SimpleDateFormat("dd-MM-yyyy'/'HH:mm:ss", Locale.getDefault())
        return timeZoneDate.format(time)
    }
}