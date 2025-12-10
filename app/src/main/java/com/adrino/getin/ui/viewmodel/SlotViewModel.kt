package com.adrino.getin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SlotViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _slots = MutableStateFlow<List<Slot>>(listOf())
    val slots: StateFlow<List<Slot>> = _slots.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<ErrorType?>(null)
    val error: StateFlow<ErrorType?> = _error.asStateFlow()

    fun fetchSlots(event: Event) {
        val eventId = event.eventId
        if (eventId.isNullOrBlank()) {
            _error.value = ErrorType.UnknownError("Event ID is required")
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getSlot(event)
            _isLoading.value = false
            
            if (result.isSuccess) {
                val allSlots = result.getOrThrow()
                _slots.value = allSlots.filter { slot -> slot.eventId == eventId }
                if (_slots.value.isEmpty()) {
                    _error.value = ErrorType.NoDataError
                }
            } else {
                val exception = result.exceptionOrNull()
                _error.value = ErrorType.NetworkError(
                    exception?.message ?: "Failed to fetch slots"
                )
            }
        }
    }
}

