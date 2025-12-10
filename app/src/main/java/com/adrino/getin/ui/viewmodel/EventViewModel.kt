package com.adrino.getin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrino.getin.data.model.Customer
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _eventList = MutableStateFlow<List<Event>>(listOf())
    val eventList: StateFlow<List<Event>> = _eventList

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    private val _selectedSlot = MutableStateFlow<com.adrino.getin.data.model.Slot?>(null)
    val selectedSlot: StateFlow<com.adrino.getin.data.model.Slot?> = _selectedSlot

    private val _isBooking = MutableStateFlow(false)
    val isBooking: StateFlow<Boolean> = _isBooking

    fun fetchEvents() {
        viewModelScope.launch {
            val result = repository.getEvents()
            if (result.isSuccess) {
                _eventList.value = result.getOrThrow()
            }
        }
    }

    fun bookSlot(
        eventId: String, slotId: String, customer: Customer,
        onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isBooking.value = true
            val result = repository.bookSlot(eventId, slotId, customer)
            _isBooking.value = false
            if (result.isSuccess) {
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Booking failed")
            }
        }
    }

    fun setSelectedEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }

    fun setSelectedSlot(slot: com.adrino.getin.data.model.Slot) {
        _selectedSlot.value = slot
    }

    fun clearSelectedSlot() {
        _selectedSlot.value = null
    }
}

