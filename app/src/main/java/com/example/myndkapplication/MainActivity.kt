package com.example.myndkapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.Utils
import org.opencv.core.CvException
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image = stringFromJNI()
        Log.d("Alpha", "size ? $image")

        val mat = image?.let { Mat(it) }
        mat?.let { imageView.setImageBitmap(convertMatToBitMap(it)) }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): Long?

    private fun convertMatToBitMap(input: Mat): Bitmap? {
        var bmp: Bitmap? = null
        val rgb = Mat()
        Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB)

        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(rgb, bmp)
        } catch (e: CvException) {
            Log.d("Exception", e.message!!)
        }

        return bmp
    }

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
    }
}
