package com.mk.core.domain.model

sealed interface StateApp<T> {
    data class Success<T>(val data: T) : StateApp<T>
    data class Error<T>(val message: String) : StateApp<T>
    class Loading<T> : StateApp<T>
    class Idle<T>: StateApp<T>
}
