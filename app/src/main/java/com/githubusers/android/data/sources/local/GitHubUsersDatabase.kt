package com.githubusers.android.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.githubusers.android.data.sources.local.converter.ListConverter

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class GitHubUsersDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {

        @Volatile
        var INSTANCE: GitHubUsersDatabase? = null

        fun getInstance(context: Context): GitHubUsersDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): GitHubUsersDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                GitHubUsersDatabase::class.java, "GitHubUsers.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}