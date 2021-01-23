package com.githubusers.android.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.githubusers.android.data.sources.local.GitHubUsersDatabase
import com.githubusers.android.data.sources.local.UserDao
import com.githubusers.android.data.sources.local.UserEntity
import com.githubusers.android.data.sources.local.updateContent
import com.githubusers.android.data.sources.remote.RemoteUserDataStore
import com.githubusers.android.data.sources.remote.response.toUserEntity
import com.githubusers.android.data.sources.remote.stores.PageUserRemoteMediator
import com.githubusers.android.domain.repositories.UserRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class UserDataRepository @Inject constructor(
    private val remoteUserDataStore: RemoteUserDataStore,
    private val database: GitHubUsersDatabase,
) : UserRepository {

    private val userDao: UserDao = database.getUserDao()

    /**
     * Concatenates two sources (Local DB and Service).
     *
     * DB source will return User from local DB if available else will return a dummy UserEntity
     * Service source will request User data from server and save it to DB.
     */
    override fun getUser(userName: String): Observable<UserEntity> {
        return userDao.getUserByUserName(userName).toObservable()
            .onErrorReturn { UserEntity(0) } // return a dummy UserEntity to swallow the error and continue the stream if there's no saved data.
            .concatWith(remoteUserDataStore.getUser(userName)
                .map {
                    val oldUserEntity = userDao.getUserByUserName(userName).blockingGet()
                    val userEntity = oldUserEntity.updateContent(it.toUserEntity())
                    userDao.insertOrReplace(userEntity)
                    userEntity
                })

    }

    override fun updateUser(userEntity: UserEntity): Completable {
        return Completable.fromCallable { userDao.update(userEntity) }
    }

    @ExperimentalPagingApi
    override fun getUsers(pageSize: Int) =
        Pager(config = PagingConfig(pageSize = 15, prefetchDistance = 10),
            remoteMediator = PageUserRemoteMediator(remoteUserDataStore, database)) {
            userDao.getUsers()
        }.flow

    override fun searchUsers(search: String) = Pager(
        PagingConfig(pageSize = 60, maxSize = 200)
    ) {
        userDao.searchUsers(search)
    }.flow


}