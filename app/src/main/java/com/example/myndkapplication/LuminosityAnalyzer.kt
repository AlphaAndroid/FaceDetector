package com.example.myndkapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

class LuminosityAnalyzer(private val context: Context, private val listener: LumaListener) :
    ImageAnalysis.Analyzer {

    private var cascFile: File? = null
    private var faceDetection: CascadeClassifier? = null

    init {
        val inputStream =
            context.resources.openRawResource(R.raw.face_cascade)
        val cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE)
        cascFile = File(cascadeDir, "haarcascade_frontalface_alt2.xml")

        val fos = FileOutputStream(cascFile)

        val buffer = ByteArray(4096)
        var bytesRead: Int = 0

        while ({ bytesRead = inputStream.read(buffer); bytesRead }() != -1) {
            fos.write(buffer, 0, bytesRead)
        }

        inputStream.close()
        fos.close()

        faceDetection = CascadeClassifier(cascFile!!.absolutePath)
        if (faceDetection?.empty() == true) {
            faceDetection = null
        } else {
            cascadeDir.delete()
        }
    }

    private fun findFace(mat: Mat): Mat {
        val matOfRect = MatOfRect()
        val emptyMat = Mat(mat.rows(), mat.cols(), CvType.CV_8UC4, Scalar(0.0, 0.0, 0.0, 0.0))
        faceDetection?.detectMultiScale(
            mat,
            matOfRect,
            1.1,
            10,
            0,
            Size(100.0, 100.0),
            Size(mat.rows().toDouble(), mat.cols().toDouble())
        )

        val faceArray = matOfRect.toArray()
        faceArray?.forEachIndexed { index, _ ->
            Imgproc.rectangle(
                emptyMat,
                faceArray[index].tl(),
                faceArray[index].br(),
                Scalar(17.0, 255.0, 0.0),
                1
            )
    }
        return emptyMat
    }

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
            var mat = yuvToMat(it)
            mat.let {
                Core.flip(mat.t(), mat, 1)
                mat = findFace(mat)
                val bmp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(mat, bmp)

                listener(bmp)
            }
        }

        imageProxy.close()
    }
}