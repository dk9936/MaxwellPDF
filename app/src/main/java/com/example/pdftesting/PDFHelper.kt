package com.example.pdftesting

import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph

class PDFHelper {
    private var callback: PDFHandlerCallback? = null



    fun generatePDF(path: String,callback: PDFHandlerCallback){

        try {
            val pdfWriter = PdfWriter(path)
            val pdfDocument = PdfDocument(pdfWriter)
            pdfDocument.addNewPage()

            val document = Document(pdfDocument)
            pdfDocument.defaultPageSize = PageSize.A4

            document.setMargins(30f, 30f, 10f, 30f)

            val text = "If you're seeing errors related to enabled = true within the viewBinding block, it's likely due to changes in the Android Gradle Plugin, and you should not manually set this flag. Instead, View Binding should be enabled by default, and you can access the generated binding classes as shown above.\n" +
                    "\n" +
                    "Ensure that you are using a recent version of Android Gradle Plugin and Android Studio to take advantage of this default View Binding feature. If you are still facing issues, it's a good idea to check for updates or consult the official Android documentation for any specific updates or issues related to your environment."
            val p1 = Paragraph(text)
            document.add(p1)
            document.add(Paragraph("\n"))


            document.add(Paragraph("\n"))

            document.close()
            callback.onSuccess()


        }catch (e: Exception){
            e.printStackTrace()
            callback.onFailed(e)
        }
    }
}