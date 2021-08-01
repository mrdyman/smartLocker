package com.macca.smartlocker.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.macca.smartlocker.Fragment.MyLockerFragment
import com.macca.smartlocker.Model.Transaction
import com.macca.smartlocker.R
import com.macca.smartlocker.SettingLockerActivity
import kotlinx.android.synthetic.main.list_my_locker.view.*

class TransactionAdapter (val Transaction : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    private lateinit var myLockerFragment : MyLockerFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_my_locker, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = Transaction[position]

        holder.namaLocker.text = transaction.Nama_Locker
        holder.mStatus.text = transaction.Locker_Status
        holder.btnSetting.setOnClickListener {
            val namaLocker = transaction.Nama_Locker
            val mulai = transaction.Mulai
            val selesai = transaction.Selesai
            val mStaus = transaction.Locker_Status
            val sisaWaktu = "23 Menit"

            //listener onClick untuk button setting
            val activity = holder.itemView.context
            showSettingLocker(activity,namaLocker, mulai, selesai, mStaus, sisaWaktu)
        }

        //listener onClick untuk button END
        holder.btnEnd.setOnClickListener {
            endLocker(transaction.Id_Locker)
        }

        //listener onClick untuk button Buka/Tutup Locker
        holder.btnOpenClose.setOnClickListener {
            openCloseLocker(transaction.Id_Locker)
        }
    }

    override fun getItemCount(): Int {
        return Transaction.size
    }

    private fun showSettingLocker(context: Context, namaLocker: String?, mulai: String?, selesai: String?, mStaus: String?, sisaWaktu: String) {
        val i = Intent(context, SettingLockerActivity::class.java)
        i.putExtra("namaLocker", namaLocker)
        i.putExtra("mulai", mulai)
        i.putExtra("selesai", selesai)
        i.putExtra("status", mStaus)
        i.putExtra("sisaWaktu", sisaWaktu)
        context.startActivity(i)
    }

    private fun endLocker(id : Long?){
        myLockerFragment = MyLockerFragment()
        myLockerFragment.endLocker(id)
    }

    private fun openCloseLocker(id: Long?){

    }

    class TransactionHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var namaLocker = itemView.tv_my_nama_locker
        var sisaWaktu = itemView.tv_sisa_waktu
        var mStatus = itemView.tv_mylcoker_status
        var btnSetting = itemView.btn_mylocker_setting
        var btnEnd = itemView.btn_end
        var btnOpenClose = itemView.btn_open_close_locker
    }
}