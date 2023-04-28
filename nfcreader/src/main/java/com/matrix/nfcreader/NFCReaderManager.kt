package com.matrix.nfcreader

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag

object NFCReaderManager : ReaderManager {
    lateinit var gotNFCData: GotNFCData
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    override fun readNFC(context: Context, gotNFCData: GotNFCData) {
        val intent = Intent(context, (context as Activity).javaClass)
            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        this.gotNFCData = gotNFCData
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (NFCUtil.isNFCEnable(nfcAdapter)) {
            pendingIntent = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    context,
                    ReaderManager.NFC_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_MUTABLE
                )
            } else  {
                PendingIntent.getActivity(
                    context,
                    ReaderManager.NFC_REQUEST_CODE,
                    intent,
                    0
                )
            }
        } else {
            this@NFCReaderManager.sendNFCData(NFCResponse.Unavailable)
            return
        }
    }

    override fun enableInForeground(activity: Activity) {
        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    override fun disableInForeground(activity: Activity) {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    override fun readFromIntent(intent: Intent) {
        val action = intent.action
        action?.let {
            when(it) {
                NfcAdapter.ACTION_TAG_DISCOVERED, NfcAdapter.ACTION_TECH_DISCOVERED -> {
                    val tagData: String? = NFCUtil.readTagData(intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG))
                    val tagId: String = NFCUtil.readIdTag(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID))
                    tagData?.let {
                        this@NFCReaderManager.sendNFCData(NFCResponse.Success(tagData))
                    } ?: run {
                        this@NFCReaderManager.sendNFCData(NFCResponse.NotRead)
                    }

                }
                NfcAdapter.ACTION_NDEF_DISCOVERED -> {
                    val message: String? = NFCUtil.readNDEFMessages(intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES))
                    val tagId: String = NFCUtil.readIdTag(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID))
                    message?.let {
                        this@NFCReaderManager.sendNFCData(NFCResponse.Success(message+tagId))
                    } ?: run {
                        this@NFCReaderManager.sendNFCData(NFCResponse.NotRead)
                    }
                }
            }
        }
    }

    private fun sendNFCData(response: NFCResponse) {
        this@NFCReaderManager.gotNFCData(this, response)
        nfcAdapter = null
        pendingIntent = null
    }

    override fun destroy() {
        nfcAdapter = null
        pendingIntent = null
    }
}
