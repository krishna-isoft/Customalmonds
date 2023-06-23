package com.isoft.customalmonds

import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.util.Log
import java.io.*

class PDFDocumentAdapter(private val file: File, private val valfilez: String) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: android.os.CancellationSignal?,
        callback: LayoutResultCallback?,
        p4: Bundle?
    ) {
        if (cancellationSignal != null) {
            if (cancellationSignal.isCanceled) {
                callback?.onLayoutCancelled()
                return
            }
        }


        val info = PrintDocumentInfo.Builder(valfilez)
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
            .build()

        callback?.onLayoutFinished(info, oldAttributes != newAttributes)
    }

    override fun onWrite(
        p0: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: android.os.CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            inputStream = FileInputStream(file)
            if (destination != null) {
                outputStream = FileOutputStream(destination.fileDescriptor)
            }

            if (outputStream != null) {
                inputStream.copyTo(outputStream)
            }

            if (cancellationSignal != null) {
                if (cancellationSignal.isCanceled) {
                    callback?.onWriteCancelled()
                } else {
                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                }
            }
        } catch (ex: Exception) {
            callback?.onWriteFailed(ex.message)
            Log.e("PDFDocumentAdapter", "Could not write: ${ex.localizedMessage}")
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }
}
