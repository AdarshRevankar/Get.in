package com.adrino.getin.data.remote

import com.adrino.getin.data.model.ApiResponse
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("event")
    suspend fun getEvents(): Response<ApiResponse<List<Event>>>

    @GET("slots")
    suspend fun getEventSlots(
        @Query("eventId")
        eventId: String
    ): Response<ApiResponse<List<Slot>>>
}

