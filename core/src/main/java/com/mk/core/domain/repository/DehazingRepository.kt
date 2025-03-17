package com.mk.core.domain.repository

import android.graphics.Bitmap

interface DehazingRepository {
    suspend fun dehazeImage(bitmap: Bitmap): Bitmap
}
