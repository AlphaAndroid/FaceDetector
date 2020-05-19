package com.example.myndkapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.opencv.android.Utils
import org.opencv.core.Core
import java.nio.ByteBuffer


class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val buffer = imageProxy.planes[0].buffer
        val data = buffer.toByteArray()
        val image = imageProxy.image

        image?.let {
            val mat = yuvToMat(it)

            mat.let {
                Core.flip(mat.t(), mat, 1)
                val bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(mat, bmp)

                listener(bmp)
            }
        }

        imageProxy.close()
    }
}