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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionAdapter (val Transaction : ArrayList<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    private lateinit var myLockerFragment : MyLockerFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_my_locker, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = Transaction[position]
        myLockerFragment = MyLockerFragment()

        val timeEnd = transaction.Selesai!!.toLong()
        val currentTime = System.currentTimeMillis()
        val waktu = timeEnd-currentTime
        val resultWaktu = waktu / 1000 / 60
        var sisaWaktu = ""
        if (timeEnd < currentTime){
            sisaWaktu = "Waktu Habis"
            myLockerFragment.endLocker(transaction.Id_Locker)
        } else {
            sisaWaktu = resultWaktu.toString() + " Menit"
        }

        val mulai = Date(transaction.Mulai!!.toLong())
        val selesai = Date(transaction.Selesai!!.toLong())
        val timeMulai = convertDate(mulai)
        val timeSelesai = convertDate(selesai)

        holder.namaLocker.text = transaction.Nama_Locker
        holder.sisaWaktu.text = sisaWaktu
        holder.mStatus.text = transaction.Locker_Status

        val lockerStatus = holder.mStatus.text
        if (lockerStatus == "LOCKED"){
            holder.btnOpenClose.text = "BUKA"
        } else {
            holder.btnOpenClose.text = "TUTUP"
        }

        holder.btnSetting.setOnClickListener {
            val idLocker = transaction.Id_Locker.toString()
            val namaLocker = transaction.Nama_Locker
            val mulai = timeMulai
            val selesai = timeSelesai
            val mStaus = transaction.Locker_Status
            val sisaWaktu = sisaWaktu

            //listener onClick untuk button setting
            val activity = holder.itemView.context
            showSettingLocker(activity, idLocker, namaLocker, mulai, selesai, mStaus, sisaWaktu)
        }

        //listener onClick untuk button END
        holder.btnEnd.setOnClickListener {
            val context = holder.itemView.context
            endLocker(transaction.Id_Locker, context)
        }

        //listener onClick untuk button Buka/Tutup Locker
        holder.btnOpenClose.setOnClickListener {
            openCloseLocker(transaction.Id_Locker)
        }
    }

    override fun getItemCount(): Int {
        return Transaction.size
    }

    private fun showSettingLocker(context: Context, idLocker : String?, namaLocker: String?, mulai: String?, selesai: String?, mStaus: String?, sisaWaktu: String) {
        val i = Intent(context, SettingLockerActivity::class.java)
        i.putExtra("idLocker", idLocker)
        i.putExtra("namaLocker", namaLocker)
        i.putExtra("mulai", mulai)
        i.putExtra("selesai", selesai)
        i.putExtra("status", mStaus)
        i.putExtra("sisaWaktu", sisaWaktu)
        context.startActivity(i)
    }

    private fun endLocker(id : Long?, context: Context){
        myLockerFragment = MyLockerFragment()
        myLockerFragment.showDialog(context, id)
    }

    private fun openCloseLocker(id: Long?){
        myLockerFragment = MyLockerFragment()
        myLockerFragment.openCloseLocker(id)
    }

    private fun convertDate(time : Date) : String{
        val timeZoneDate = SimpleDateFormat("dd-MM-yyyy'/'HH:mm:ss", Locale.getDefault())
        return timeZoneDate.format(time)
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