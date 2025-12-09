package com.adrino.getin.data.model

data class Slot(
    val id: String,
    val eventId: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
)