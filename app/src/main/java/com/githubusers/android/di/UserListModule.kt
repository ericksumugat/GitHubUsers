package com.githubusers.android.di

import com.githubusers.android.data.repositories.UserDataRepository
import com.githubusers.android.presentation.user.list.UserListViewModel
import dagger.Module
import dagger.Provides

@Module
class UserListModule  {

    @Provides
    @PerActivity
    fun provideUserListViewModel(userDataRepository: UserDataRepository): UserListViewModel {
        return UserListViewModel(userDataRepository)
    }


}