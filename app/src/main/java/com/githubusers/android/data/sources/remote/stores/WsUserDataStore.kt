package com.githubusers.android.data.sources.remote.stores

import com.githubusers.android.data.exceptions.extensions.mapNetworkErrors
import com.githubusers.android.data.exceptions.extensions.retryOnError
import com.githubusers.android.data.sources.remote.GitHubService
import com.githubusers.android.data.sources.remote.RemoteUserDataStore
import com.githubusers.android.data.sources.remote.response.UserResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class WsUserDataStore @Inject constructor(private val gitHubService: GitHubService) :
    RemoteUserDataStore {

    override fun getUser(userName: String): Observable<UserResponse> =
        gitHubService.getUser(userName)
            .mapNetworkErrors() // An extension that maps network error to application exceptions
            .retryOnError(maxRetry = 3) // An extension that retries the operation on error.


    override fun getUsers(since: Int): Observable<List<UserResponse>> =
        gitHubService.getUsers(since)
            .mapNetworkErrors()
            .retryOnError(maxRetry = 3)
}