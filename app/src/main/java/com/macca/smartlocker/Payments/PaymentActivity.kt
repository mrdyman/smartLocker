package com.macca.smartlocker.Payments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        SdkUIFlowBuilder.init()
            .setClientKey("SB-Mid-client-VC8itBKdhlacu-pD")
            .setContext(applicationContext)
            .setTransactionFinishedCallback(TransactionFinishedCallback {
                result ->
                if (result.status == "success"){
                    //put logic here when transaction is success
                } else if (result.status == "pending"){
                    //put logic here when transaction is pending
                } else if (result.status == "failed"){
                    //put logic here when transaction is failed
                }
            })
            .setMerchantBaseUrl("http://192.168.43.26:80/smartlocker/index.php/")
            .enableLog(true)
            .setColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .setLanguage("id")
            .buildSDK()

        btn_pesan.setOnClickListener {
            val transactionRequest = TransactionRequest("MaccaLab-"+System.currentTimeMillis().toString(), 10000.00)
            val itemDetails = ItemDetails("ItemId", 10000.00, 1, "Locker-1")
            val itemDetailList = ArrayList<ItemDetails>()
            itemDetailList.add(itemDetails)

            transactionDetail(transactionRequest)
            transactionRequest.itemDetails = itemDetailList

            MidtransSDK.getInstance().transactionRequest = transactionRequest

            MidtransSDK.getInstance().startPaymentUiFlow(this)
        }
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
}