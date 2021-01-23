package com.githubusers.android.data.sources.remote.stores

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.rxjava3.RxRemoteMediator
import com.githubusers.android.data.exceptions.network.NetworkException
import com.githubusers.android.data.exceptions.network.NoNetworkException
import com.githubusers.android.data.sources.local.GitHubUsersDatabase
import com.githubusers.android.data.sources.local.UserEntity
import com.githubusers.android.data.sources.remote.RemoteUserDataStore
import com.githubusers.android.data.sources.remote.response.toUserEntity
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

@ExperimentalPagingApi
class PageUserRemoteMediator @Inject constructor(
    private val remoteUserDataStore: RemoteUserDataStore,
    private val database: GitHubUsersDatabase,
) : RemoteMediator<Int, UserEntity>() {

    private val userDao = database.getUserDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>,
    ): MediatorResult {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val since: Int = when (loadType) {
                    LoadType.REFRESH -> 0 // 0 to start from the top of the user's list.
                    LoadType.APPEND -> {
                        // Return the id of the last item if not null else end the pagination since there are no more items to load.
                        state.lastItemOrNull()?.id ?: return@withContext MediatorResult.Success(
                            endOfPaginationReached = true)
                    }
                    LoadType.PREPEND -> return@withContext MediatorResult.Success(true) // Prepend not needed so just return success.
                }

                val response = remoteUserDataStore.getUsers(since = since).blockingFirst()

                database.runInTransaction {
                    userDao.insertOrUpdate(response.map { it.toUserEntity() })
                }

                MediatorResult.Success(endOfPaginationReached = response.isEmpty())
            } catch (e: NoNetworkException) {
                MediatorResult.Error(e)
            } catch (e: NetworkException) {
                MediatorResult.Error(e)
            }
        }
    }
}