package com.example.maxwellpdf.util

import android.content.Context
import android.os.Environment
import java.io.*

object FileUtils {
    @Throws(IOException::class)
    fun fileFromAsset(context: Context, assetName: String): File {
        val outFile = File(context.cacheDir, "$assetName")
        if (assetName.contains("/")) {
            outFile.parentFile.mkdirs()
        }
        copy(context.assets.open(assetName), outFile)
        return outFile
    }

    @Throws(IOException::class)
    fun copy(inputStream: InputStream?, output: File?) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(output)
            var read = 0
            val bytes = ByteArray(1024)
            while (inputStream!!.read(bytes).also { read = it } != -1) {
                outputStream.write(bytes, 0, read)
            }
        } finally {
            try {
                inputStream?.close()
            } finally {
                outputStream?.close()
            }
        }
    }

    private fun copyFileToExternalStorage(
        sourceFilePath: String,
        destinationFileName: String,
        fileName: String,
        pdfViewerCallback: PDFViewerCallback
    ) {
        try {
            val timestamp = System.currentTimeMillis()
            val uniqueFileName = "$fileName-$timestamp.pdf"
            val destinationFile = File("$destinationFileName$uniqueFileName")

            if (destinationFile.exists()) {
                // Handle the case when a file with the same name already exists
                pdfViewerCallback.onFailed(Exception("File with the same name already exists"))
            } else {
                val inputStream = FileInputStream(sourceFilePath)
                val outputStream = FileOutputStream(destinationFile)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } > 0) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                inputStream.close()
                outputStream.close()
                pdfViewerCallback.onSuccess("Saved in Internal Storage/Document")
            }
        } catch (e: Exception) {
            pdfViewerCallback.onFailed(e)
        }
    }


    @Throws(IOException::class)
    fun downloadFile(sourceFilePath: String, directoryName: String, fileName: String, pdfViewerCallback: PDFViewerCallback) {
        val dirPath = getAppPath(directoryName)
        val outFile = File(dirPath)
        if (!outFile.exists()) {
            outFile.mkdirs()
        }
        copyFileToExternalStorage(sourceFilePath, dirPath, fileName,pdfViewerCallback)

    }

    // It provides a path of InternalStorage/Document/AppName/file.pdf
    private fun getAppPath(directoryName: String): String {

        val documentsDirectory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
            directoryName
        )

        if (!documentsDirectory.exists()) {
            try {
                documentsDirectory.mkdir()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return documentsDirectory.path + File.separator
    }
}