package com.example.myndkapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.OpenCVLoader
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        String((stringFromJNI()), Charset.defaultCharset())
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): ByteArray

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
