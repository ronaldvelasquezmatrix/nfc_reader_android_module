package com.matrix.nfc_card_reader

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.matrix.nfc_card_reader.databinding.ActivityMainBinding
import com.matrix.nfcreader.NFCReaderManager
import com.matrix.nfcreader.NFCResponse
import com.matrix.nfcreader.ReaderManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
    }

    private fun setNavigation() {
        binding.bnvNfcReader.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.page_nfc -> {
                    val intent = Intent(this@MainActivity, NfcAnimationActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}