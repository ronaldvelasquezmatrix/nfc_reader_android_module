package com.matrix.nfc_card_reader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.matrix.nfc_card_reader.databinding.ActivityMainBinding
import com.matrix.nfc_card_reader.databinding.ActivityNfcresponseBinding

class NFCResponseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNfcresponseBinding
    companion object {
        const val DATA = "data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcresponseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nfcData = intent.getStringExtra(DATA)
        binding.txtNfcData.text = nfcData
    }
}