package com.mk.core.di

import com.mk.core.data.DCPRepositoryImpl
import com.mk.core.domain.repository.DCPRepository
import com.mk.core.domain.usecase.DehazingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModuleApp {

    @Provides
    @Singleton
    fun provideDCPRepository(): DCPRepository {
        return DCPRepositoryImpl()
    }

    @Provides
    fun provideDehazingUseCase(repository: DCPRepository): DehazingUseCase {
        return DehazingUseCase(repository)
    }
}