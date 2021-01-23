package com.githubusers.android.data.sources.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOrReplace(obj: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(objs: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun update(objs: List<T>)

    /**
     * Make sure that the field annotated with [androidx.room.PrimaryKey]
     * is populated before using this method.
     */
    @Update
    abstract fun update(vararg obj: T)
}