package com.adrino.getin.data.remote

import com.adrino.getin.data.model.ApiResponse
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("mockResponse/events.json")
    suspend fun getEvents(): Response<ApiResponse<List<Event>>>

    @GET("mockResponse/slots.json")
    suspend fun getEventSlots(
        @Query("eventId")
        eventId: String
    ): Response<ApiResponse<List<Slot>>>

    @POST("mockResponse/book")
    suspend fun bookSlot(
        @Body param: HashMap<String, Any>
    ): Response<ApiResponse<Unit>>

    @GET("mockResponse/book.json")
    suspend fun bookSlotMock(): Response<ApiResponse<Unit>>
}

