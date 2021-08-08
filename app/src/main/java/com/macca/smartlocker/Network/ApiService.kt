package com.macca.smartlocker.Network

import com.macca.smartlocker.Model.PaymentStatus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //get Payment status
    @GET("v2/{orderId}/status")
    fun getPaymentStatus(@Path("orderId") orderId : String) : Call<PaymentStatus>
}