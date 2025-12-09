package com.adrino.getin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adrino.getin.data.remote.RetrofitClient
import com.adrino.getin.data.repository.EventRepository
import com.adrino.getin.ui.component.EventCard
import com.adrino.getin.ui.theme.GetInTheme
import com.adrino.getin.ui.viewmodel.EventViewModel
import com.adrino.getin.ui.viewmodel.EventViewModelFactory

class HomeActivity : ComponentActivity() {

    private val repository = EventRepository(RetrofitClient.apiService)
    private val viewModel: EventViewModel by viewModels { EventViewModelFactory(repository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetInTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    val events by viewModel.eventList.collectAsState()

                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(events) { event ->
                            EventCard(event = event)
                        }
                    }
                }

            }
        }
    }
}
