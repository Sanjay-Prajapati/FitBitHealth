package com.app.fitbithealth.di

import com.app.fitbithealth.shareddata.repo.UserRepo
import com.app.fitbithealth.shareddata.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideUserRepo(userRepo: UserRepository): UserRepo = userRepo
}