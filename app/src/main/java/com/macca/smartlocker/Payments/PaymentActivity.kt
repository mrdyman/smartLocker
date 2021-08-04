package com.macca.smartlocker.Payments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.macca.smartlocker.MainActivity
import com.macca.smartlocker.R
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.corekit.models.snap.Transaction
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        midtransInit()

        val transactionId = "MaccaLab-"+ System.currentTimeMillis().toString()
        displayDetailTransaction(transactionId)

        btn_pesan.setOnClickListener {
            val transactionRequest = TransactionRequest(transactionId, 10000.00)
            val itemDetails = ItemDetails("ItemId", 10000.00, 1, "Locker-1")
            val itemDetailList = ArrayList<ItemDetails>()
            itemDetailList.add(itemDetails)

            transactionDetail(transactionRequest)
            transactionRequest.itemDetails = itemDetailList

            MidtransSDK.getInstance().transactionRequest = transactionRequest

            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }

        btn_edit_pesanan.setOnClickListener {
            this.onBackPressed()
        }

        btn_transaction_back_to_home.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        //get data user ke firebase
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance("https://smartlocker-7f844-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(user: DataSnapshot) {
                if (user.exists()){
                    for (mUser in user.children){
                        val mUserId = mUser.child("user_id").value
                        if (userId.toString() == mUserId.toString()){
                            val firstName = mUser.child("nama_depan").value
                            val lastName = mUser.child("nama_belakang").value
                            val email = mUser.child("email").value
                            val alamat = mUser.child("alamat").value
                            val city = mUser.child("kota").value
                            val postalCode = mUser.child("kode_pos").value
                            val phoneNumber = mUser.child("phone").value
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
    }

    private fun midtransInit(){
        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-VC8itBKdhlacu-pD")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(TransactionFinishedCallback {
                    result ->
                if (result.status == "success"){
                    //put logic here when transaction is success
                    Log.d("MidtransLog", "transaction is successful")
                } else if (result.status == "pending"){
                    //put logic here when transaction is pending
                    Log.d("MidtransLog", "transaction is pending")
                } else if (result.status == "failed"){
                    //put logic here when transaction is failed
                    Log.d("MidtransLog", "transaction is failed")
                }
            })
            .setMerchantBaseUrl("http://192.168.43.26:80/smartlocker/index.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FF3700B3", "#FF3700B3", "#FF3700B3"))
            .setLanguage("id")
            .buildSDK()
    }

    private fun transactionDetail(transactionRequest: TransactionRequest) {
        val customerDetail = CustomerDetails()
        customerDetail.customerIdentifier = "Dyman-userId"
        customerDetail.phone = "081322899246"
        customerDetail.firstName = "Andi Mardiman"
        customerDetail.lastName = "Saputra"
        customerDetail.email = "andimardiman@macca.com"

        val shippingAddress = ShippingAddress()
        shippingAddress.address = "Jl. R.E. Martadinata - Tondo"
        shippingAddress.city = "Palu"
        shippingAddress.postalCode = "94138"
        customerDetail.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = "Jl. R.E. Martadinata - Tondo"
        billingAddress.city = "Palu"
        billingAddress.postalCode = "94138"
        customerDetail.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetail
    }

    private fun displayDetailTransaction(idTransaction : String?){
        val namaLocker = this.intent.getStringExtra("namaLocker")
        val durasi = this.intent.getStringExtra("durasi")
        tv_nama_locker_detail_transaction.text = namaLocker
        tv_transaction_id.text = idTransaction
        tv_durasi_locker.text = "Durasi : "+ durasi
    }
}