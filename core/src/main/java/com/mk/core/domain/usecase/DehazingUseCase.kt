package com.mk.core.domain.usecase

import android.graphics.Bitmap
import android.util.Log
import com.mk.core.domain.model.StateApp
import com.mk.core.domain.repository.DCPRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DehazingUseCase(
    private val repository: DCPRepository
) {
    operator fun invoke(bitmap: Bitmap): Flow<StateApp<Bitmap>> = flow  {
        emit(StateApp.Loading())
        try {
            val result = repository.dehazeImage(bitmap)
            emit(StateApp.Success(result))
        } catch (e: Exception) {
            Log.e(TAG, "invoke: $e")
            emit(StateApp.Error(e.message ?: "Terjadi kesalahan saat dehazing"))
        }
    }

    companion object{
        const val TAG = "DehazingUseCase"
    }
}
