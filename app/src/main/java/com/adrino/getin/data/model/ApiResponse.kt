package com.adrino.getin.data.model

data class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val success: Boolean = false,
    val error: String? = null
)

