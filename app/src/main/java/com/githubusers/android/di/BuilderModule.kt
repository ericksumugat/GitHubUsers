package com.githubusers.android.di

import com.githubusers.android.presentation.user.list.UserListActivity
import com.githubusers.android.presentation.user.profile.UserProfileActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [UserListModule::class])
    abstract fun bindUserListActivity(): UserListActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [UserProfileModule::class])
    abstract fun bindUUserProfileActivity(): UserProfileActivity
}


