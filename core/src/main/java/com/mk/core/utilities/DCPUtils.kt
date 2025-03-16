package com.mk.core.utilities

import android.util.Log
import org.opencv.android.OpenCVLoader

object DCPUtils {
    private const val TAG = "DCPUtils"
    fun initOpenCV() {
        if (!OpenCVLoader.initLocal()) {
            Log.e(TAG, "Gagal menginisialisasi OpenCV!")
        } else {
            Log.d(TAG, "OpenCV berhasil diinisialisasi!")
        }
    }
}