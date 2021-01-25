package com.githubusers.android.data.sources.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Single

@Dao
abstract class UserDao : BaseDao<UserEntity>() {

    @Query("SELECT * FROM user WHERE login = :userName")
    abstract fun getUserByUserNameSingle(userName: String): Single<UserEntity>

    @Query("SELECT * FROM user WHERE login = :userName")
    abstract fun getUserByUserName(userName: String): UserEntity?

    @Query("SELECT * FROM user WHERE login LIKE '%' || :searchInput || '%' OR note LIKE '%' || :searchInput || '%'")
    abstract fun searchUsers(searchInput: String): PagingSource<Int, UserEntity>

    @Query("SELECT * FROM user  ORDER BY id ASC")
    abstract fun getUsers(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM user")
    abstract fun deleteAll()

    @Query("SELECT * FROM user WHERE id IN (:ids)")
    abstract fun getUsers(ids: List<Int>): List<UserEntity>

    @Transaction
    open fun insertOrUpdate(objList: List<UserEntity>) {
        val insertResult = insert(objList) // try to insert all
        val updateList = mutableListOf<UserEntity>()

        // search for items that were ignored since there's already an existing entry
        for (i in insertResult.indices) {
            if (insertResult[i] == -1L) updateList.add(objList[i])
        }

        // Get the entries first and update the content to prevent deleting values that are not included in response like Note, following, followers, and etc.
        if (updateList.isNotEmpty()) {
            val users = getUsers(updateList.map { it.id })
            val updatedUsers = users.map {  oldContent ->
                val newContent = updateList.firstOrNull { oldContent.id == it.id }
                newContent?.let { oldContent.updateContent(it) } ?: oldContent
            }
            update(updatedUsers)
        }
    }
}