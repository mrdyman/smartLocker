package com.macca.smartlocker.Payments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.Model.PaymentStatus
import com.macca.smartlocker.Network.ApiConfig
import com.macca.smartlocker.R
import com.macca.smartlocker.Util.BroadcastReceiver
import com.macca.smartlocker.Util.SmartLockerSharedPreferences
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PaymentActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    private lateinit var smartLockerSharedPreferences: SmartLockerSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        smartLockerSharedPreferences = SmartLockerSharedPreferences(this)

        //get transaction unique id
        val transactionId = smartLockerSharedPreferences.transactionId

        //get lockerId data (used to fill ItemId)
        val itemId = smartLockerSharedPreferences.itemId

        //get nama locker (used to fill itemName)
        val itemName = smartLockerSharedPreferences.itemName

        //get durasi locker
        val durasi = smartLockerSharedPreferences.duration

        //initialize midtrans
        midtransInit()

        //call function to display detail transaction
        displayDetailTransaction(itemName, transactionId, durasi)

        //handle event when button proses pembayaran clicked
        val context = this
        buttonPayListener(context, transactionId!!, itemId, itemName)

        //handle event when button edit pesanan and back to home clicked
        buttonListener()
    }

    private fun midtransInit(){
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-VC8itBKdhlacu-pD")
            .setContext(applicationContext)
            .setTransactionFinishedCallback { result ->
                val itemId = smartLockerSharedPreferences.itemId
                val durasi = smartLockerSharedPreferences.duration
                if (result.status == "success") {
                    //put logic here when transaction is success
                    Log.d("MidtransLog", "transaction is successful")

                    //insert data ke tabel transaction dengan status running
                    insertDataTransaction(itemId, durasi)

                } else if (result.status == "pending") {
                    //put logic here when transaction is pending
                    Log.d("MidtransLog", "transaction is pending")

                    //update status locker di firebase menjadi "pending"
                    updateLockerStatus(itemId!!, "Pending")

                    //call API Midtrans
                    getPaymentStatus()

                } else if (result.status == "failed") {
                    //put logic here when transaction is failed
                    Log.d("MidtransLog", "transaction is failed")
                }
            }
            .setMerchantBaseUrl("http://192.168.43.26:80/smartlocker/index.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FF3700B3", "#FF3700B3", "#FF3700B3"))
            .setLanguage("id")
            .buildSDK()
    }

    private fun buttonPayListener(context: Context, transactionId : String, itemId: String?, itemName: String?){
        btn_pesan.setOnClickListener {
            val transactionRequest = TransactionRequest(transactionId, 10000.00)

            //get data user ke firebase
            auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid
            databaseReference = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Users")

            databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(user: DataSnapshot) {
                    if (user.exists()){
                        for (mUser in user.children){
                            val mUserId = mUser.child("user_id").value
                            if (userId.toString() == mUserId.toString()){
                                val firstName = mUser.child("nama_depan").value.toString()
                                val lastName = mUser.child("nama_belakang").value.toString()
                                val email = mUser.child("email").value.toString()
                                val alamat = mUser.child("alamat").value.toString()
                                val city = mUser.child("kota").value.toString()
                                val postalCode = mUser.child("kode_pos").value.toString()
                                val phoneNumber = mUser.child("phone").value.toString()


                                val itemDetails = ItemDetails(itemId, 10000.00, 1, itemName)
                                val itemDetailList = ArrayList<ItemDetails>()
                                itemDetailList.add(itemDetails)

                                //call transaction detail to fill data user before send to midtrans
                                //all user data from firebase passed to this function
                                transactionDetail(transactionRequest, userId!!, firstName, lastName,email, alamat, city, postalCode, phoneNumber)
                                transactionRequest.itemDetails = itemDetailList


                                MidtransSDK.getInstance().transactionRequest = transactionRequest

                                MidtransSDK.getInstance().startPaymentUiFlow(context)
                            }
                        }
                    } else {
                        Log.d("paymentActivity", "Failed to get user data from firebase")
                    }
                }

                override fun onCancelled(dataBaseError: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            finish()
        }
    }

    private fun transactionDetail(
        transactionRequest: TransactionRequest,
        customerId: String,
        firstName: String,
        lastName: String,
        email: String,
        alamat: String,
        kota: String,
        kodePos: String,
        phone: String
    ) {
        val customerDetail = CustomerDetails()
        customerDetail.customerIdentifier = customerId
        customerDetail.phone = phone
        customerDetail.firstName = firstName
        customerDetail.lastName = lastName
        customerDetail.email = email

        val shippingAddress = ShippingAddress()
        shippingAddress.address = alamat
        shippingAddress.city = kota
        shippingAddress.postalCode = kodePos
        customerDetail.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = alamat
        billingAddress.city = kota
        billingAddress.postalCode = kodePos
        customerDetail.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetail
    }

    private fun displayDetailTransaction(namaLocker : String?, idTransaction : String?, durasi : String?){
        tv_nama_locker_detail_transaction.text = namaLocker
        tv_transaction_id.text = idTransaction
        tv_durasi_locker.text = "Durasi : "+ durasi
    }

    private fun buttonListener(){
        btn_edit_pesanan.setOnClickListener {
            this.onBackPressed()
        }

        btn_transaction_back_to_home.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun updateLockerStatus(idLocker : String, status : String?){
        databaseReference = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Locker")

        val locker = databaseReference.child(idLocker)

        locker.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataLocker: DataSnapshot) {
                if (dataLocker.exists()){
                    val lockerStatus = dataLocker.child("Status").value
                    if (lockerStatus == status){
                        Log.d("PaymentActivity", "Locker status with id $idLocker already $status")
                    } else {
                        //update status locker ke jenis status, pending/booked(sesuai parameter yang dikirimkan)
                        databaseReference.child(idLocker).child("Status").setValue(status)
                        Log.d("PaymentActivity", "Locker status with id $idLocker has updated to $status")
                    }
                } else {
                    Log.d("PaymentActivity", "Locker with id $idLocker is not found.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("PaymentActivity", "database Error with message ${databaseError.message}")
            }

        })
    }

    private fun insertDataTransaction(idLocker : String?, durasi : String?){
        databaseReference = FirebaseDatabase.getInstance("https://smart-locker-f9a91-default-rtdb.firebaseio.com/").getReference("Transaction")

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        val lockerId = idLocker?.toLong()
        val waktu = durasi
        val mulai = System.currentTimeMillis()
        var covertDurasi : Long = 0

        //convert waktu yang dipilih user
        when(waktu){
            "30 Menit" -> covertDurasi = 30 * 1000 * 60
            "60 Menit" -> covertDurasi = 60 * 1000 * 60
            "120 Menit" -> covertDurasi = 120 * 1000 * 60
        }

        val selesai = mulai + covertDurasi
        val namaLocker = "Locker "+ idLocker
        val lockerStatus = "LOCKED"
        val transactionStatus = "Running"

        val data = com.macca.smartlocker.Model.Transaction(userId, lockerId, mulai.toString(), namaLocker, selesai.toString(), lockerStatus, transactionStatus, waktu)
        val transaction = databaseReference.child(userId.toString())

        transaction.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(mtransaction: DataSnapshot) {
                val transactionId = mtransaction.childrenCount + 1

                transaction.child(transactionId.toString()).setValue(data).addOnSuccessListener {
                    Log.d("PaymentActivity", "Successfully insert data transaction.")

                    //update status locker menjadi "Booked"
                    updateLockerStatus(lockerId.toString(), "Booked")
                } .addOnFailureListener {
                    Log.d("transactionStatus", "Failed to insert data transaction.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getPaymentStatus(){
        paymentStatus(this)
    }

    fun paymentStatus(context: Context){
        smartLockerSharedPreferences = SmartLockerSharedPreferences(context)
        val orderId = smartLockerSharedPreferences.transactionId
        val itemId = smartLockerSharedPreferences.itemId
        val durasi = smartLockerSharedPreferences.duration
        val client = ApiConfig.getApiService().getPaymentStatus(orderId!!)
        client.enqueue(object : Callback<PaymentStatus>{
            override fun onResponse(call: Call<PaymentStatus>, response: Response<PaymentStatus>) {
                if (response.isSuccessful){
                    val data = response.body()
                    val transactionStatus = data?.transactionStatus
                    if (transactionStatus == "pending"){
                        //transaksi pending (user belum bayar)
                        //jalankan broadcastreceiver untuk mengecek status pembayaran dalam waktu 5, 10, dan 15 menit
                        Log.d("broadcast_orderId", response.body().toString())
                        checkCurrentPayment(context, true)
                    } else if (transactionStatus == "settlement"){
                        Log.d("broadcast_orderId", "transaction status is $transactionStatus, updating locker status to Booked")
                        updateLockerStatus(itemId!!, "Booked")
                        insertDataTransaction(itemId, durasi)
                        checkCurrentPayment(context, false)
                    }
                    else {
                        Log.d("broadcast_orderId", "transaction is $transactionStatus, stop checking payment status")
                        updateLockerStatus(itemId!!, "Ready")
                        Log.d("broadcast_orderId", "Locker status has been updated to ready")
                        checkCurrentPayment(context, false)
                    }
                }
            }

            override fun onFailure(call: Call<PaymentStatus>, t: Throwable) {
                Log.d("PaymentActivity", "failed connect to API with message ${t.printStackTrace()}")
            }

        })
    }

    fun checkCurrentPayment(context: Context, alarmStatus : Boolean){

        if (alarmStatus){
            enableBroadcastReceiver(context)
            smartLockerSharedPreferences = SmartLockerSharedPreferences(context)
            val orderId = smartLockerSharedPreferences.transactionId
            val currentTime = System.currentTimeMillis()
            val executeTime = currentTime + 300000
            val intervalTime = 300000 / 1000 / 60
            Log.d("broadcast_orderId", "paymentActivity: "+orderId)
            Log.d("broadcast_orderId", "current Time : "+currentTime.toString())
            Log.d("broadcast_orderId", "execute Time : "+executeTime.toString())
            Log.d("broadcast_orderId", "interval Time : $intervalTime Minutes")

            val i = Intent(this, BroadcastReceiver::class.java)
            i.putExtra("order_id", orderId)
            val pi = PendingIntent.getBroadcast(context, 111, i, 0)
            val am : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.setRepeating(AlarmManager.RTC_WAKEUP, currentTime, 300000, pi)
        } else {
            disableBroadcastReceiver(context)
            Log.d("broadcast_orderId", "alarm has been shutdown")
        }
    }

    fun disableBroadcastReceiver(context: Context) {
        val receiver = ComponentName(context, BroadcastReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
//        Toast.makeText(context, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show()
    }

    fun enableBroadcastReceiver(context: Context) {
        val receiver = ComponentName(context, BroadcastReceiver::class.java)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
//        Toast.makeText(context, "Enabled broadcast receiver", Toast.LENGTH_SHORT).show()
    }
}