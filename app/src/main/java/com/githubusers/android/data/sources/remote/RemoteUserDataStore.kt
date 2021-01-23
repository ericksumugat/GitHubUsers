package com.githubusers.android.data.sources.remote

import com.githubusers.android.data.sources.remote.response.UserResponse
import io.reactivex.rxjava3.core.Observable

interface RemoteUserDataStore {

    fun getUser(userName: String): Observable<UserResponse>

    fun getUsers(since: Int): Observable<List<UserResponse>>
}