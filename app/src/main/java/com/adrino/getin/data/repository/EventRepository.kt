package com.adrino.getin.data.repository

import com.adrino.getin.data.model.ApiResponse
import com.adrino.getin.data.model.Customer
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Response

class EventRepository(private val apiService: ApiService) {

    suspend fun getEvents(): Result<List<Event>> = withContext(Dispatchers.IO) {
        handleResponse(apiService.getEvents())
    }

    suspend fun getSlot(event: Event): Result<List<Slot>> = withContext(Dispatchers.IO) {
        handleResponse(apiService.getEventSlots(event.eventId!!))
    }

    suspend fun bookSlot(eventId: String, slotId: String, customer: Customer): Result<Unit> = withContext(Dispatchers.IO) {
        val param = hashMapOf<String, Any>()
        param["eventId"] = eventId
        param["slotId"] = slotId
        param["customerName"] = customer.name
        param["phoneNumber"] = customer.phoneNumber
        delay(5000)
        // Mocking Response
        // handleResponse(apiService.bookSlot(param))
        handleBookResponse(apiService.bookSlotMock())
    }

    private fun handleBookResponse(
        response: Response<ApiResponse<Unit>>,
    ): Result<Unit> {
        return if (response.isSuccessful && response.body() != null) {
            val apiResponse = response.body()
            if (apiResponse?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(apiResponse?.message ?: "Unknown error"))
            }
        } else {
            Result.failure(Exception("Failed to book: ${response.message()}"))
        }
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

