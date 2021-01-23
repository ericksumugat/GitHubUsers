package com.githubusers.android.domain.repositories

import androidx.paging.PagingData
import com.githubusers.android.data.sources.local.UserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun updateUser(userEntity: UserEntity): Completable

    fun getUser(userName: String): Observable<UserEntity>

    fun getUsers(pageSize: Int): Flow<PagingData<UserEntity>>

    fun searchUsers(search: String) : Flow<PagingData<UserEntity>>
}