package com.adrino.getin.navigation

import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot

sealed class NavRoute(val route: String) {

    object Home : NavRoute("home")

    data class EventDetail(val event: Event) : NavRoute("event_detail/{eventId}") {
        companion object {
            const val route = "event_detail/{eventId}"
            fun createRoute(eventId: String) = "event_detail/$eventId"
        }
    }

    data class Review(val event: Event, val slot: Slot) : NavRoute("review/{eventId}/{slotId}") {
        companion object {
            const val route = "review/{eventId}/{slotId}"
            fun createRoute(eventId: String, slotId: String) = "review/$eventId/$slotId"
        }
    }

    object Confirmation : NavRoute("confirmation")
}

