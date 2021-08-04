package com.macca.smartlocker.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Fragment.LockerFragment
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.Locker
import com.macca.smartlocker.R
import kotlinx.android.synthetic.main.list_available_locker.view.*

class LockerAdapter (val Locker : ArrayList<Locker>) : RecyclerView.Adapter<LockerAdapter.LockerHolder>() {

    private lateinit var lockerFragment: LockerFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_available_locker, parent, false)
        return LockerHolder(view)
    }

    override fun onBindViewHolder(holder: LockerHolder, position: Int) {
        val locker = Locker[position]

        holder.namaLocker.text = locker.Nama
        holder.mStatus.text = locker.Status

        holder.btnAddtoCart.setOnClickListener {
            val idLocker = locker.Id
            val namaLocker = locker.Nama
            showTimePicker(holder.itemView.context,idLocker, namaLocker)
        }
    }

    override fun getItemCount(): Int {
        return Locker.size
    }

    class LockerHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLocker = itemView.tv_nama_locker
        var mStatus = itemView.tv_locker_status
        var btnAddtoCart = itemView.btn_add_to_cart
    }

    private fun showTimePicker(context : Context, idLocker: Long?, namaLocker : String?) {
        lockerFragment = LockerFragment()
        lockerFragment.showDialog(context ,idLocker, namaLocker)
    }

}