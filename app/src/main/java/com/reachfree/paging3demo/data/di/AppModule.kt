package com.reachfree.paging3demo.data.di

import com.reachfree.paging3demo.data.network.UserApi
import com.reachfree.paging3demo.data.repository.UserRepository
import com.reachfree.paging3demo.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideUserApi(): UserApi = UserApi()

    @Provides
    fun provideUserRepository(api: UserApi): UserRepository = UserRepositoryImpl(api)



}