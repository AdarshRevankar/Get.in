package com.adrino.getin.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.ui.component.EventDetailTopBar
import com.adrino.getin.ui.component.EventInfoCard
import com.adrino.getin.ui.component.EventWallpaperHeader
import com.adrino.getin.ui.component.slotsSection
import com.adrino.getin.ui.viewmodel.SlotViewModel

@Composable
fun EventDetailScreen(
    event: Event,
    viewModel: SlotViewModel,
    onBackClick: () -> Unit,
    onSlotClick: (Slot) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val slots by viewModel.slots.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(event) {
        viewModel.fetchSlots(event)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EventDetailTopBar(onBackClick = onBackClick)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            EventWallpaperHeader(
                event = event,
                height = 350.dp,
                modifier = Modifier.zIndex(0f)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .zIndex(1f),
                contentPadding = PaddingValues(
                    top = 200.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            ) {
                item {
                    EventInfoCard(event = event)
                }

                slotsSection(
                    slots = slots,
                    isLoading = isLoading,
                    error = error,
                    onSlotClick = onSlotClick
                )
            }
        }
    }
}

