package com.adrino.getin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adrino.getin.data.repository.EventRepository

class SlotViewModelFactory(
    private val repository: EventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlotViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SlotViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

