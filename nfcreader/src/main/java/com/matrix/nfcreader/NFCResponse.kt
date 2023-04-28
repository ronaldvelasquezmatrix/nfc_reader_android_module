package com.matrix.nfcreader

sealed class NFCResponse {
    data class Success(val successData: String): NFCResponse()
    object Unavailable: NFCResponse()
    object NotRead: NFCResponse()
    data class InvalidateError(val message: String): NFCResponse()
}