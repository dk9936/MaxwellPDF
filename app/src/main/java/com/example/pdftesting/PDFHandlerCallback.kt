package com.example.pdftesting

interface PDFHandlerCallback {

    fun onFailed(e: Exception)
    fun onSuccess()

}