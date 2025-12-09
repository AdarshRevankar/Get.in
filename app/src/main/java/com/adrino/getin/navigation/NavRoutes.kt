package com.adrino.getin.navigation

import com.adrino.getin.data.model.Event

sealed class NavRoute(val route: String) {

    object Home : NavRoute("home")

    data class EventDetail(val event: Event) : NavRoute("event_detail/{eventId}") {
        companion object {
            const val route = "event_detail/{eventId}"
            fun createRoute(eventId: String) = "event_detail/$eventId"
        }
    }
}

