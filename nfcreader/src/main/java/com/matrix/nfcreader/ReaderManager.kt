package com.matrix.nfcreader

import android.app.Activity
import android.content.Context
import android.content.Intent

interface ReaderManager {
    companion object {
        const val NFC_REQUEST_CODE: Int = 1000
    }
    fun readNFC(context: Context, gotNFCData: GotNFCData)
    fun enableInForeground(activity: Activity)
    fun disableInForeground(activity: Activity)
    fun readFromIntent(intent: Intent)
    fun destroy()
}
