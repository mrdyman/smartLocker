package com.macca.smartlocker.Util

import android.content.Context

class SmartLockerSharedPreferences (val context: Context) {

    companion object {
        private const val TRANSACTION_ID = "TRANSACTION_ID"
        private const val ITEM_ID = "ITEM_ID"
        private const val ITEM_NAME = "ITEM_NAME"
        private const val DURATION = "DURATION"
    }

    private val p = context.getSharedPreferences(context.packageName+"_Preferences", Context.MODE_PRIVATE)

    var transactionId = p.getString(TRANSACTION_ID, null)
        set(value) = p.edit().putString(TRANSACTION_ID, value).apply()

    var itemId = p.getString(ITEM_ID, null)
        set(value) = p.edit().putString(ITEM_ID, value).apply()

    var itemName = p.getString(ITEM_NAME, null)
        set(value) = p.edit().putString(ITEM_NAME, value).apply()

    var duration = p.getString(DURATION, null)
        set(value) = p.edit().putString(DURATION, value).apply()
}