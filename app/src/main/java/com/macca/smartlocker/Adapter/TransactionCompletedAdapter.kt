package com.macca.smartlocker.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.list_completed_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionCompletedAdapter (val transactionCompleted : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionCompletedAdapter.TransactionCompletedHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionCompletedHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_completed_transaction, parent, false)
        return TransactionCompletedHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionCompletedHolder, position: Int) {
        val transactionCompleted = transactionCompleted[position]

        val time = Date(transactionCompleted.Selesai!!.toLong())
        val completedTransactionTime = convertDate(time)

        holder.namaLockerCompleted.text = transactionCompleted.Nama_Locker
        holder.dateTransactionCompleted.text = completedTransactionTime

    }

    override fun getItemCount(): Int {
        return transactionCompleted.size
    }

    class TransactionCompletedHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLockerCompleted = itemView.tv_nama_locker_completed
        var dateTransactionCompleted = itemView.tv_date_transaction_completed
    }

    private fun convertDate(time : Date) : String{
        val timeZoneDate = SimpleDateFormat("dd-MM-yyyy'/'HH:mm:ss", Locale.getDefault())
        return timeZoneDate.format(time)
    }
}