package com.githubusers.android.di

import com.githubusers.android.data.sources.remote.RemoteUserDataStore
import com.githubusers.android.data.sources.remote.stores.WsUserDataStore
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataStoreModule {

    @Singleton
    @Binds
    abstract fun bindRemoteUserDataStore(userDataStore: WsUserDataStore): RemoteUserDataStore
}