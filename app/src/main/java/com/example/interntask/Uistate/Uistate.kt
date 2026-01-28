package com.example.interntask.Uistate

import com.example.interntask.model.Bannermodel.PhotoResponse

sealed interface Uistate<out T> {
    class Loading(): Uistate<Nothing>
    data class Success<T>(var data:T): Uistate<T>
    data class Error (var message:String): Uistate<Nothing>

}