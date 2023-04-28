package com.matrix.nfcreader

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Parcelable
import java.util.Arrays

typealias GotNFCData = (NFCReaderManager, NFCResponse) -> Unit

object NFCUtil {
    internal fun readNDEFMessages(rawMessages: Array<Parcelable>?): String? {
        rawMessages?.let {
            return if (it.isEmpty()) {
                null
            } else {
                var messageData = ""
                val messages = arrayOfNulls<NdefMessage>(it.size)
                for (i in it.indices) {
                    messages[i] = it[i] as NdefMessage
                }
                for(message in messages) {
                    message?.let {
                        for(record in message.records) {
                            val data = getDataFromRecord(record)
                            messageData += data + "\n"
                        }
                    }
                }
               messageData
            }
        } ?: run {
            return null
        }
    }

    fun isNFCEnable(adapter: NfcAdapter?): Boolean {
        return if (adapter != null) {
            adapter!!.isEnabled
        } else {
            false
        }
    }

    internal fun readTagData(tag: Tag?): String? {
        tag?.let {
            val tagDataBuilder = StringBuilder()
            val techList = it.techList
            var techString: String = ""
            for (data in techList) {
                techString += "${data},"
            }
            tagDataBuilder.append("Tech: ${techString}\n")
                .append("Descripcion: ${tag.toString()}\n")
                .append("ID: ${getStringFromByteArray(tag.id)}\n")
            return tagDataBuilder.toString()
        } ?: run {
            return null
        }
    }

    private fun getStringFromByteArray(bytes: ByteArray) : String {
        val dataString = bytes.joinToString(separator = ":" ) {
            "%02x".format(it)
        }
        return dataString
    }

    private fun getDataFromRecord(record: NdefRecord) : String {
        val data = StringBuilder()
        val text = String(record.payload)
        val type = String(record.type)
        NdefRecord.TNF_WELL_KNOWN
        val id = record.id.joinToString(separator = ":" ) {
            "%02x".format(it)
        }
        data.append("data: $text \n")
        data.append("type: $type \n")
        data.append("TNF: ${record.tnf} \n")
        return data.toString()
    }

    fun readIdTag(byteArrayExtra: ByteArray?): String {
        byteArrayExtra?.let {
            return "id: " + getStringFromByteArray(it)
        }
        return ""
    }
}
