package com.macca.smartlocker.Network

import com.macca.smartlocker.Model.PaymentStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //get Payment status
    @GET("{orderId}/status")
    fun getPaymentStatus(@Query("orderId") orderId : String) : Call<PaymentStatus>
}