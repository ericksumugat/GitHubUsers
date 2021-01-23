package com.githubusers.android.di

import com.githubusers.android.data.repositories.UserDataRepository
import com.githubusers.android.presentation.user.profile.UserProfileViewModel
import dagger.Module
import dagger.Provides

@Module
class UserProfileModule {
    @Provides
    @PerActivity
    fun provideUserListViewModel(userDataRepository: UserDataRepository): UserProfileViewModel {
        return UserProfileViewModel(userDataRepository)
    }
}