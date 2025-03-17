package com.mk.core.data

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.createBitmap
import com.mk.core.algorithm.DCP
import com.mk.core.domain.repository.DehazingRepository
import org.opencv.android.Utils
import org.opencv.core.Mat
import javax.inject.Inject

class DehazingRepositoryImpl @Inject constructor() : DehazingRepository {

    private fun dcpDehazing(bitmap: Bitmap): Bitmap {
        try {
            val processedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

            val inputMat = bitmapToMat(processedBitmap)
            val outputMat = DCP.removeHaze(inputMat)

            val outputBitmap = createBitmap(processedBitmap.width, processedBitmap.height)
            matToBitmap(outputMat, outputBitmap)

            inputMat.release()
            outputMat.release()

            return outputBitmap
        } catch (e: Exception) {
            Log.e(TAG, "Error dehazing image: ${e.message}")
            throw e
        }
    }

    private fun bitmapToMat(bitmap: Bitmap): Mat {
        val mat = Mat()
        try {
            Utils.bitmapToMat(bitmap, mat)
        } catch (e: Exception) {
            Log.e(TAG, "Error converting Bitmap to Mat: ${e.message}")
        }
        return mat
    }

    private fun matToBitmap(mat: Mat, bitmap: Bitmap) {
        try {
            Utils.matToBitmap(mat, bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "Error converting Mat to Bitmap: ${e.message}")
        }
    }

    override suspend fun dehazeImage(
        bitmap: Bitmap,
    ): Bitmap {
        return try {
             dcpDehazing(bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "Error in dehazeImage: ${e.message}")
            throw e
        }
    }

    companion object{
        const val TAG = "DCPRepositoryImpl"
    }
}
