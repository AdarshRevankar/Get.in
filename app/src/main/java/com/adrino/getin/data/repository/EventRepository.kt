package com.adrino.getin.data.repository

import com.adrino.getin.data.model.ApiResponse
import com.adrino.getin.data.model.Customer
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class EventRepository(private val apiService: ApiService) {

    suspend fun getEvents(): Result<List<Event>> = withContext(Dispatchers.IO) {
        try {
            handleResponse(apiService.getEvents())
        } catch (e: IOException) {
            Result.failure(IOException("Network error: ${e.message}", e))
        } catch (e: HttpException) {
            Result.failure(IOException("HTTP error: ${e.code()}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}", e))
        }
    }

    suspend fun getSlot(event: Event): Result<List<Slot>> = withContext(Dispatchers.IO) {
        val eventId = event.eventId
        if (eventId.isNullOrBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Event ID cannot be null or empty"))
        }
        
        try {
            handleResponse(apiService.getEventSlots(eventId))
        } catch (e: IOException) {
            Result.failure(IOException("Network error: ${e.message}", e))
        } catch (e: HttpException) {
            Result.failure(IOException("HTTP error: ${e.code()}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}", e))
        }
    }

    suspend fun bookSlot(
        eventId: String,
        slotId: String,
        customer: Customer
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val param = hashMapOf<String, Any>(
                "eventId" to eventId,
                "slotId" to slotId,
                "customerName" to customer.name,
                "phoneNumber" to customer.phoneNumber
            )
            delay(5000)
            // Mocking Response - replace with actual API call
            // handleBookResponse(apiService.bookSlot(param))
            handleBookResponse(apiService.bookSlotMock())
        } catch (e: IOException) {
            Result.failure(IOException("Network error: ${e.message}", e))
        } catch (e: HttpException) {
            Result.failure(IOException("HTTP error: ${e.code()}", e))
        } catch (e: Exception) {
            Result.failure(Exception("Unexpected error: ${e.message}", e))
        }
    }

    private fun handleBookResponse(
        response: Response<ApiResponse<Unit>>
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
        response: Response<ApiResponse<T>>
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

