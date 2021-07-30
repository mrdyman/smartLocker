package com.macca.smartlocker.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.fragment_my_locker.view.*

class TransactionAdapter (val Transaction : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_my_locker, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = Transaction[position]

        holder.namaLocker.text = transaction.Nama_Locker
        holder.mstatus.text = transaction.Locker_Status
    }

    override fun getItemCount(): Int {
        return Transaction.size
    }

    class TransactionHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLocker = itemView.tv_my_nama_locker
        var sisaWaktu = itemView.tv_sisa_waktu
        var mstatus = itemView.tv_mylcoker_status
    }
}