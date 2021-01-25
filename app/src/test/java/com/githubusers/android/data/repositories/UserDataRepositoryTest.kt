package com.githubusers.android.data.repositories

import androidx.room.rxjava3.EmptyResultSetException
import com.githubusers.android.data.sources.local.GitHubUsersDatabase
import com.githubusers.android.data.sources.local.UserDao
import com.githubusers.android.data.sources.local.UserEntity
import com.githubusers.android.data.sources.remote.RemoteUserDataStore
import com.githubusers.android.data.sources.remote.response.UserResponse
import com.githubusers.android.data.sources.remote.response.toUserEntity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import kotlin.math.log

class UserDataRepositoryTest {

    @RelaxedMockK
    lateinit var userDao: UserDao

    @RelaxedMockK
    lateinit var database: GitHubUsersDatabase

    @RelaxedMockK
    lateinit var remoteUserDataStore: RemoteUserDataStore

    private lateinit var userDataRepository: UserDataRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { database.getUserDao() }.returns(userDao)

        userDataRepository = UserDataRepository(remoteUserDataStore, database)
    }

    @Test
    fun `get user, no saved data in db`(){
        every { userDao.getUserByUserNameSingle(any()) }.returns(Single.error(EmptyResultSetException("No Match")))
        every { userDao.getUserByUserName(any()) }.returns(null)
        every { remoteUserDataStore.getUser(any())}.returns(Observable.just(createUserResponse()))

        val userName = "JohnSm8"
        userDataRepository.getUser(userName)
            .test()
            .assertComplete()

        verify {
            userDao.getUserByUserNameSingle(withArg { it.equals(userName, false)  })
            remoteUserDataStore.getUser(withArg { it.equals(userName, false)  })
            userDao.getUserByUserName(withArg { it.equals(userName, false)  })
            userDao.insertOrReplace(withArg { it.login.equals(userName, false)  })
        }
    }

    @Test
    fun `get user, with saved data in db`(){
        val userName = "JohnSm8"
        val user = UserEntity(id = 1, login = userName)
        val userResponse = createUserResponse()

        every { userDao.getUserByUserNameSingle(any()) }.returns(Single.just(user))
        every { userDao.getUserByUserName(any()) }.returns(user)
        every { remoteUserDataStore.getUser(any())}.returns(Observable.just(userResponse))

        userDataRepository.getUser(userName)
            .test()
            .assertComplete()
            .assertValueCount(2)

        verify {
            userDao.getUserByUserNameSingle(withArg { it.equals(userName, false)  })
            remoteUserDataStore.getUser(withArg { it.equals(userName, false)  })
            userDao.getUserByUserName(withArg { it.equals(userName, false)  })

            // verify that the old content was updated
            userDao.insertOrReplace(withArg {
                it.login.equals(userName, false) && it.following == 1 && it.followers == 1
            })
        }
    }

    @Test
    fun `update user`() {
        val entity = UserEntity(id = 1)
        userDataRepository.updateUser(entity)
            .test()
            .assertComplete()

        verify {
            userDao.update(entity)
        }
    }

    @Test
    fun `search users`() {
        // TODO Don't know yet how to test Paging 3 written codes
    }

    @Test
    fun `get users`() {
        // TODO Don't know yet how to test Paging 3 written codes
    }

    private fun createUserResponse(): UserResponse {
        return UserResponse(
            id = 1,
            login = "JohnSm8",
            avatarUrl = "",
            eventsUrl = "",
            followersUrl = "",
            followingUrl = "",
            gistsUrl = "",
            gravatarId = "",
            htmlUrl = "",
            nodeId = "",
            organizationsUrl = "",
            receivedEventsUrl = "",
            reposUrl = "",
            siteAdmin = false,
            starredUrl = "",
            subscriptionsUrl = "",
            type = "",
            url = "",
            followers = 1,
            following = 1,
            name = "",
            company = "",
            blog = "",
            location = "",
            email = "",
            twitterUserName = ""
        )
    }

}