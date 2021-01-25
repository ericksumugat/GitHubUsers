package com.githubusers.android.data.sources.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var userDao: UserDao
    private lateinit var db: GitHubUsersDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, GitHubUsersDatabase::class.java
        ).build()
        userDao = db.getUserDao()
    }

    @Test
    fun writeAndGetByUsername() {
        val userName = "JohnSm8"
        val user = UserEntity(id = 1, login = userName)

        userDao.insert(user)
        userDao.getUserByUserNameSingle(userName)
            .test()
            .assertComplete()
            .assertValue { it.login.equals(userName, false) }
    }

    @Test
    fun getUsers() {
        val users = listOf(
            UserEntity(id = 1, login = "JohnSm8"),
            UserEntity(id = 2, login = "Heen")
        )

        userDao.insert(users)
        val result = userDao.getUsers(listOf(1, 2))
        assert(result.size == users.size)
    }


    @Test
    fun insertOrUpdate(){
        val users = listOf(
            UserEntity(id = 1, login = "JohnSm8", note = "Test"),
            UserEntity(id = 2, login = "Heen", note = "Test")
        )

        userDao.insert(users)
        userDao.insertOrUpdate(users.map {
            it.note = null
            it
        })

        val results = userDao.getUsers(listOf(1,2))

        results.forEach {
            assert(it.note.equals("Test", false))
        }
    }

    @Test
    fun searchPagingUsers() {
        // TODO Don't know yet how to test Paging 3 written codes
    }

    @Test
    fun getPagingUsers() {
        // TODO Don't know yet how to test Paging 3 written codes
    }

}