package com.mk.core.domain.repository

import android.graphics.Bitmap

interface DCPRepository {
    suspend fun dehazeImage(bitmap: Bitmap): Bitmap
}
