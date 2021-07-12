package com.example.pictureoftheday.api.epic

sealed class EPICData {
    data class Success(val serverResponseData: EPICServerResponseData) : EPICData()
    data class Error(val error: Throwable) : EPICData()
    data class Loading(val progress: Int?) : EPICData()
}