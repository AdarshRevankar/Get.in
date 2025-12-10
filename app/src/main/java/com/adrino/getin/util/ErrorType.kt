package com.adrino.getin.util

sealed class ErrorType {
    data class NetworkError(val message: String) : ErrorType()
    data class ServerError(val message: String) : ErrorType()
    data class UnknownError(val message: String) : ErrorType()
    object NoDataError : ErrorType()
    
    val errorMessage: String
        get() = when (this) {
            is NetworkError -> message
            is ServerError -> message
            is UnknownError -> message
            is NoDataError -> "No data available"
        }
}

