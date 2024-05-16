package dev.ohjiho.budgetify.data.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<E> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: E): Long

    @Update
    suspend fun update(entity: E)

    @Delete
    suspend fun delete(vararg entity: E)
}