package com.adrino.getin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SlotViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _slots = MutableStateFlow<List<Slot>>(listOf())
    val slots: StateFlow<List<Slot>> = _slots

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchSlots(event: Event) {
        if (event.eventId == null) {
            _error.value = "Event ID is null"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getSlot(event)
            _isLoading.value = false
            
            if (result.isSuccess) {
                _slots.value = result.getOrThrow().filter { slot ->  slot.eventId == event.eventId }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to fetch slots"
            }
        }
    }
}

