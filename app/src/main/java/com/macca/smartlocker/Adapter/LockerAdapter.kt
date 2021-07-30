package com.macca.smartlocker.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Model.Locker
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.list_available_locker.view.*

class LockerAdapter (val Locker : ArrayList<Locker>) : RecyclerView.Adapter<LockerAdapter.LockerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_available_locker, parent, false)
        return LockerHolder(view)
    }

    override fun onBindViewHolder(holder: LockerHolder, position: Int) {
        val locker = Locker[position]

        holder.namaLocker.text = locker.Nama
        holder.mStatus.text = locker.Status
    }

    override fun getItemCount(): Int {
        return Locker.size
    }

    class LockerHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLocker = itemView.tv_nama_locker
        var mStatus = itemView.tv_locker_status
    }

}