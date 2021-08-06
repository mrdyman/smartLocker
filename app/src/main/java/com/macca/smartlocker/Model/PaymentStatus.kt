package com.macca.smartlocker.Model

import com.google.gson.annotations.SerializedName

data class PaymentStatus(

	@field:SerializedName("transaction_id")
	val transactionId: String? = null,

	@field:SerializedName("fraud_status")
	val fraudStatus: String? = null,

	@field:SerializedName("status_message")
	val statusMessage: String? = null,

	@field:SerializedName("transaction_status")
	val transactionStatus: String? = null,

	@field:SerializedName("status_code")
	val statusCode: String? = null,

	@field:SerializedName("signature_key")
	val signatureKey: String? = null,

	@field:SerializedName("gross_amount")
	val grossAmount: String? = null,

	@field:SerializedName("merchant_id")
	val merchantId: String? = null,

	@field:SerializedName("settlement_time")
	val settlementTime: String? = null,

	@field:SerializedName("payment_type")
	val paymentType: String? = null,

	@field:SerializedName("transaction_time")
	val transactionTime: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null
)
