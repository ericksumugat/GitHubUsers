
package com.githubusers.android.di

import android.content.Context
import com.githubusers.android.data.sources.local.GitHubUsersDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Provides
    @Singleton
    fun bindEnglishCentralDatabase(context: Context): GitHubUsersDatabase =
        GitHubUsersDatabase.getInstance(context)
}