package com.githubusers.android.presentation

import com.githubusers.android.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class GitHubUsersApplication: DaggerApplication(){

    override fun applicationInjector(): AndroidInjector<GitHubUsersApplication> {
        return DaggerAppComponent
            .builder()
            .application(this)
            .context(this)
            .build()
    }


}