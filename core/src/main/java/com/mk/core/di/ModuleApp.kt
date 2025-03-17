package com.mk.core.di

import com.mk.core.data.DehazingRepositoryImpl
import com.mk.core.domain.repository.DehazingRepository
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
    fun provideDCPRepository(): DehazingRepository {
        return DehazingRepositoryImpl()
    }

    @Provides
    fun provideDehazingUseCase(repository: DehazingRepository): DehazingUseCase {
        return DehazingUseCase(repository)
    }
}