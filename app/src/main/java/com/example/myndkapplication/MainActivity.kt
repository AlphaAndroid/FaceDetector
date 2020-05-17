package com.example.myndkapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {

        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    //private external fun getImageFromPath(): Long

    //private external fun setNativeMat(mat: Long, width: Int, height: Int, isIncrease: Boolean)
}
