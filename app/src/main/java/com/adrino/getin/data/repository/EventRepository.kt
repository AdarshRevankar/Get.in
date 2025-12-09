package com.adrino.getin.data.repository

import com.adrino.getin.data.model.ApiResponse
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class EventRepository(private val apiService: ApiService) {

    suspend fun getEvents(): Result<List<Event>> = withContext(Dispatchers.IO) {
        handleResponse(apiService.getEvents())
    }

    suspend fun getSlot(event: Event): Result<List<Slot>> = withContext(Dispatchers.IO) {
        handleResponse(apiService.getEventSlots(event.eventId!!))
    }

    private fun <T> handleResponse(
        response: Response<ApiResponse<T>>,
    ): Result<T> {
        return if (response.isSuccessful && response.body() != null) {
            val apiResponse = response.body()
            if (apiResponse?.success == true && apiResponse.data != null) {
                Result.success(apiResponse.data)
            } else {
                Result.failure(Exception(apiResponse?.message ?: "Unknown error"))
            }
        } else {
            Result.failure(Exception("Failed to fetch: ${response.message()}"))
        }
    }
}

