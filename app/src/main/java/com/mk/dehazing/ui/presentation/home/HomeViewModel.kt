package com.mk.dehazing.ui.presentation.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mk.core.domain.model.StateApp
import com.mk.core.domain.usecase.DehazingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dehazingUseCase: DehazingUseCase
): ViewModel() {
    private val _state = MutableStateFlow<StateApp<Bitmap>>(StateApp.Idle())
    val state: StateFlow<StateApp<Bitmap>> get() = _state

    fun dehazeImage(bitmap: Bitmap) {
        viewModelScope.launch {
            dehazingUseCase(bitmap).collect { result ->
                _state.value = result
            }
        }
    }

    fun resetState(){
        _state.value = StateApp.Idle()
    }

}