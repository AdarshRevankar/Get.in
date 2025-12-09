package com.adrino.getin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _eventList = MutableStateFlow<List<Event>>(listOf())
    val eventList: StateFlow<List<Event>> = _eventList

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    fun fetchEvents() {
        viewModelScope.launch {
            val result = repository.getEvents()
            if (result.isSuccess) {
                _eventList.value = result.getOrThrow()
            }
        }
    }

    fun setSelectedEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }
}

