package com.example.pdftesting

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maxwellpdf.PdfViewerActivity

class MainActivity : AppCompatActivity(), PDFHandlerCallback {
    companion object {
        private const val TAG = "MainActivity"
    }
    private val requiredPermissionList = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private val PERMISSION_CODE = 4040
    private var path = ""
    private val bGeneratePDF: Button
        get() = findViewById(R.id.b_generate_pdf)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        bGeneratePDF.setOnClickListener {
            try {
                val folder = "ConsumerDetailsPdf"
                val fileName = "demo.pdf"
                path = getExternalFilesDir(folder).toString() + "/" + fileName

                val pdfHelper = PDFHelper()

                pdfHelper.generatePDF(path, this)
            }catch (e: Exception){
                Log.d(TAG, "onCreate: ${e.cause}")
            }
            


        }

    }

    override fun onFailed(e: Exception) {
        Log.d(TAG, "onFailed: ${e.message}")
    }

    override fun onSuccess() {
        Log.d(TAG, "onSuccess: $path")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            launchPdf()
        }else{
            if(checkAndRequestPermission())
                launchPdf()
        }




    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val readPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (readPermission && writePermission)
                    launchPdf()
                else {
                    Toast.makeText(this, " Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun checkAndRequestPermission(): Boolean {
        val permissionsNeeded = ArrayList<String>()

        for (permission in requiredPermissionList) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                permissionsNeeded.add(permission)
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded.toTypedArray(),
                PERMISSION_CODE
            )
            return false
        }

        return true
    }

    private fun launchPdf() {

        startActivity(
            PdfViewerActivity.launchPdfFromPath(
                context = this,
                path,
                pdfTitle = "MyPDF",
                directoryName = "DineshDIr",
                enableDownload = true
            )
        )
    }
}