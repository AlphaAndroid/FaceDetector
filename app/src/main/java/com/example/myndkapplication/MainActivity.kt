package com.example.myndkapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.Utils
import org.opencv.core.CvException
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class MainActivity : AppCompatActivity() {

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val image = getImageFromPath()
        //try {
        //    val mat = Mat(image)
        //    imageView.setImageBitmap(convertMatToBitMap(mat))
        //} catch (exception: UnsupportedOperationException) {
        //    Toast.makeText(
        //        this,
        //        "Loading error. Image exist? or permission granted?",
        //        Toast.LENGTH_LONG
        //    ).show()
        //}
        //val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
        Log.d("Alpha", "0")
        sendBitmapToNative(bitmap)
        Log.d("Alpha", "1")
        //imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 150, 150, false))
        Log.d("Alpha", "2")

        Log.d("Alpha", "test 0")
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        setNativeMat(mat.nativeObjAddr, 15000, 15000, false)
        Log.d("Alpha", "test 1")
        Bitmap.createScaledBitmap(bitmap, 15000, 15000, false)
        Log.d("Alpha", "test 2")
    }

    private fun sendBitmapToNative(bitmap: Bitmap) {
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        setNativeMat(mat.nativeObjAddr, 150, 150, false)
        try {
            //val matImage = Mat(image)
            imageView.setImageBitmap(convertMatToBitMap(mat))
        } catch (exception: UnsupportedOperationException) {
            Toast.makeText(
                this,
                "Loading error. Image exist? or permission granted?",
                Toast.LENGTH_LONG
            ).show()
        }
    }

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

    private external fun getImageFromPath(): Long

    private external fun setNativeMat(mat: Long, width: Int, height: Int, isIncrease: Boolean)
}
