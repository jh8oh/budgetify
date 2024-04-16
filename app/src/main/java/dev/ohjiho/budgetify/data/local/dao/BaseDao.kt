package dev.ohjiho.budgetify.data.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base Data Access Object that other DAOs should implement
 */
interface BaseDao<E> {
    /**
     * Inserts an entity in the database
     *
     * @param entity - The entity to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: E): Long

    /**
     * Updates an entity in the database
     *
     * @param entity - The entity to be updated
     */
    @Update
    suspend fun update(entity: E)

    /**
     * Deletes a variable amount of entities in the database
     *
     * @param entity - The entity to be deleted
     */
    @Delete
    suspend fun delete(vararg entity: E)
}