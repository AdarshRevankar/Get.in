package com.adrino.getin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrino.getin.data.model.Customer
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.data.repository.EventRepository
import com.adrino.getin.util.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _eventList = MutableStateFlow<List<Event>>(listOf())
    val eventList: StateFlow<List<Event>> = _eventList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error.asStateFlow()

    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent.asStateFlow()

    private val _selectedSlot = MutableStateFlow<Slot?>(null)
    val selectedSlot: StateFlow<Slot?> = _selectedSlot.asStateFlow()

    private val _isBooking = MutableStateFlow(false)
    val isBooking: StateFlow<Boolean> = _isBooking.asStateFlow()

    fun fetchEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getEvents()
            _isLoading.value = false
            if (result.isSuccess) {
                _eventList.value = result.getOrThrow()
            } else {
                val exception = result.exceptionOrNull()
                _error.value = ErrorType.NetworkError(
                    exception?.message ?: "Failed to fetch events"
                )
            }
        }
    }

    fun bookSlot(
        eventId: String,
        slotId: String,
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _isBooking.value = true
            _error.value = null
            
            val result = repository.bookSlot(eventId, slotId, customer)
            _isBooking.value = false
            
            if (result.isSuccess) {
                onSuccess()
            } else {
                val exception = result.exceptionOrNull()
                val errorMessage = exception?.message ?: "Booking failed"
                _error.value = ErrorType.ServerError(errorMessage)
                onError(errorMessage)
            }
        }
    }

    fun setSelectedEvent(event: Event) {
        _selectedEvent.value = event
    }

    fun clearSelectedEvent() {
        _selectedEvent.value = null
    }

    fun setSelectedSlot(slot: Slot) {
        _selectedSlot.value = slot
    }

    fun clearSelectedSlot() {
        _selectedSlot.value = null
    }

    fun clearError() {
        _error.value = null
    }
}

