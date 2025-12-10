package com.adrino.getin.di

import com.adrino.getin.data.remote.ApiService
import com.adrino.getin.data.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideEventRepository(apiService: ApiService): EventRepository {
        return EventRepository(apiService)
    }
}

