package com.adrino.getin.utils

fun formatTimeToAMPM(time24: String?): String {
    if (time24.isNullOrBlank()) return ""

    return try {
        val parts = time24.split(":")
        if (parts.size != 2) return time24

        val hour = parts[0].toInt()
        val minute = parts[1]

        when {
            hour == 0 -> "12:$minute AM"
            hour < 12 -> "$hour:$minute AM"
            hour == 12 -> "12:$minute PM"
            else -> "${hour - 12}:$minute PM"
        }
    } catch (_: Exception) {
        time24
    }
}

