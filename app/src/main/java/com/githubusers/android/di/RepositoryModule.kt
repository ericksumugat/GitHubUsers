package com.githubusers.android.di

import com.githubusers.android.data.repositories.UserDataRepository
import com.githubusers.android.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepository: UserDataRepository): UserRepository
}