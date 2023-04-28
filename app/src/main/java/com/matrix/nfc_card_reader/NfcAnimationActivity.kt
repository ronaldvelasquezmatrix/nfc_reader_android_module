package com.matrix.nfc_card_reader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matrix.nfcreader.NFCReaderManager
import com.matrix.nfcreader.NFCResponse
import com.matrix.nfcreader.ReaderManager

class NfcAnimationActivity : AppCompatActivity() {
    private lateinit var readerManager: ReaderManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_animation)
        setNfcReader()
    }

    private fun setNfcReader() {
        readerManager = NFCReaderManager
        readerManager.readNFC(this@NfcAnimationActivity) { adapter, result ->
            when (result) {
                is NFCResponse.Success -> {
                    val intent = Intent(this@NfcAnimationActivity, NFCResponseActivity::class.java)
                    intent.putExtra(NFCResponseActivity.DATA, result.successData)
                    startActivity(intent)
                    finish()

                }
                is NFCResponse.NotRead -> {
                    Log.d("NFC READER", "NotRead")

                }
                is NFCResponse.Unavailable -> {
                    Log.d("NFC READER", "Unavailable")
                    readerManager.destroy()
                }
                is NFCResponse.InvalidateError -> {
                    Log.d("NFC READER", "InvalidateError")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        readerManager.enableInForeground(this)
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            readerManager.readFromIntent(it)
        }
        super.onNewIntent(intent)
    }

    override fun onPause() {
        readerManager.disableInForeground(this)
        super.onPause()
    }

    override fun onDestroy() {
        readerManager.destroy()
        super.onDestroy()
    }

}