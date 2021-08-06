package com.macca.smartlocker.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.list_completed_transaction.view.*
import kotlinx.android.synthetic.main.list_pending_transaction.view.*

class TransactionPendingAdapter (val transactionPending : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionPendingAdapter.TransactionPendingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionPendingHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_pending_transaction, parent, false)
        return TransactionPendingHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionPendingHolder, position: Int) {
        val transactionPending = transactionPending[position]

        holder.namaLockerPending.text = transactionPending.Nama_Locker
        holder.dateTransactionPending.text = transactionPending.Mulai
        holder.deadlinePayment.text = transactionPending.Mulai + transactionPending.Waktu
        holder.btnHowToPay.setOnClickListener {
            //tampilkan dialog tutorial cara pembayaran
        }

    }

    override fun getItemCount(): Int {
        return transactionPending.size
    }

    class TransactionPendingHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLockerPending = itemView.tv_nama_locker_pending
        var dateTransactionPending = itemView.tv_date_transaction_pending
        var deadlinePayment = itemView.tv_deadline_transaction_pending
        var btnHowToPay = itemView.btn_how_to_pay_transaction
    }
}