package com.githubusers.android.di

import android.app.Application
import android.content.Context
import com.githubusers.android.presentation.GitHubUsersApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    NetworkModule::class,
    DataStoreModule::class,
    StorageModule::class,
    RepositoryModule::class,
    BuilderModule::class])
interface AppComponent : AndroidInjector<GitHubUsersApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }

    override fun inject(app: GitHubUsersApplication)
}