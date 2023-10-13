package com.example.maxwellpdf.util

import java.lang.Exception

interface PDFViewerCallback {

    fun onFailed(exception: Exception)
    fun onSuccess(message: String)
}