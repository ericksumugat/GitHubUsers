package com.githubusers.android.data.sources.remote

import com.githubusers.android.data.sources.remote.response.UserResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {

    companion object {
        const val HOST = "https://api.github.com/"
    }

    @GET("users")
    fun getUsers(@Query("since") since: Int): Observable<List<UserResponse>>

    @GET("users/{userName}")
    fun getUser(@Path("userName") userName: String): Observable<UserResponse>

}