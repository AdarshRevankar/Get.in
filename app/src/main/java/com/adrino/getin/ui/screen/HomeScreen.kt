package com.adrino.getin.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.adrino.getin.navigation.NavRoute
import com.adrino.getin.ui.component.EventCard
import com.adrino.getin.ui.viewmodel.EventViewModel

@Composable
fun HomeScreen(navController: NavController, eventViewModel: EventViewModel) {
    val events by eventViewModel.eventList.collectAsState()

    LaunchedEffect(Unit) {
        eventViewModel.fetchEvents()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(events) { event ->
                EventCard(event = event) {
                    eventViewModel.setSelectedEvent(event)
                    navController.navigate(NavRoute.EventDetail.createRoute(event.eventId ?: ""))
                }
            }
        }
    }
}