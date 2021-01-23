package com.githubusers.android.domain.repositories

import androidx.paging.PagingData
import com.githubusers.android.data.sources.local.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * Updates the user entry
     */
    fun updateUser(userEntity: UserEntity): Completable


    /**
     * Concatenates two sources (Local DB and Service).
     *
     * DB source will return User from local DB if available else will return a dummy UserEntity
     * Service source will request User data from server and save it to DB.
     */
    fun getUser(userName: String): Observable<UserEntity>


    /**
     * Retrieves users from DB and services. Managed by paging 3 library
     *
     * https://developer.android.com/topic/libraries/architecture/paging/v3-overview
     */
    fun getUsers(pageSize: Int): Flow<PagingData<UserEntity>>

    /**
     * Searches DB for user's username or note that matches or contains the @param search
     */
    fun searchUsers(search: String) : Flow<PagingData<UserEntity>>
}